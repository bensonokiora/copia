package co.ke.copia

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.ke.copia.adapter.AllocationsAdapter
import co.ke.copia.databinding.ActivityMainBinding
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem
import co.ke.copia.repo.CopiaDatabase
import co.ke.copia.repo.LocalRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var data_receipt: ArrayList<ReceiptItem>
    private lateinit var data_payments: ArrayList<PaymentItem>
    private lateinit var data_allocation: ArrayList<AllocationItem>

    private var receiptAmount: Int = 0;
    private var mpesaAmount: Int = 0
    private var allocatedAmount = 0

    private val TAG = MainActivity::class.java.simpleName


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val receiptsDAO = CopiaDatabase.getInstance(application).receiptsDAO
        val transactionsDAO = CopiaDatabase.getInstance(application).transactionsDAO
        val allocationDAO = CopiaDatabase.getInstance(application).allocationDAO

        val repository = LocalRepository(receiptsDAO, transactionsDAO, allocationDAO);
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

        mainViewModel.message.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        mainViewModel.getReceipts().observe(this) {
            getReceipts(it)

        }
        mainViewModel.getTransactions().observe(this) {
            getTransactions(it)
        }
        mainViewModel.getAllocations().observe(this) {
            getAllocations(it)
        }

        binding.save.setOnClickListener {
            insertAllocation()
        }

        binding.fabDeleteAll.setOnClickListener {
            deleteAllocations()
        }

    }

    private fun getReceipts(it: List<ReceiptItem>?) {
        if (it != null) {
            if (it.isNotEmpty()) {
                data_receipt = java.util.ArrayList()
                data_receipt.addAll(it)
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item,
                    data_receipt
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.receiptSpinner.adapter = adapter
                Log.d(TAG, "Adding data to spinner Receipts")
            }
        }
    }

    private fun getTransactions(it: List<PaymentItem>?) {
        if (it != null) {
            if (it.isNotEmpty()) {
                data_payments = java.util.ArrayList()
                data_payments.addAll(it)
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item,
                    data_payments
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.mpesaRefSpinner.adapter = adapter
                Log.d(TAG, "Adding data to spinner Mpesa Transactions")
            }
        }
    }

    private fun getAllocations(it: List<AllocationItem>) {
        data_allocation = java.util.ArrayList()
        binding.allocationLayout.allocationParent.visibility = View.GONE
        if (it.isNotEmpty()) {
            binding.allocationLayout.allocationParent.visibility = View.VISIBLE
            data_allocation.addAll(it)

            val adapter = AllocationsAdapter(data_allocation)
            val lm = LinearLayoutManager(this)
            binding.allocationLayout.recycler.layoutManager = lm
            binding.allocationLayout.recycler.adapter = adapter

            Log.d(TAG, "Adding allocation data to recyclerview")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun insertAllocation() {
        binding.logs.text = null
        if (TextUtils.isEmpty(binding.amount.text)) {
            binding.logs.text = "Oops! You haven't entered any allocation amount "
            return
        }
        allocatedAmount = binding.amount.text.toString().toInt()
        if (data_receipt.size > 0) {
            for (i in data_receipt.indices) {
                if (binding.receiptSpinner.selectedItem.toString() == data_receipt[i].receipt
                ) {
                    receiptAmount = data_receipt[i].amount_to_be_paid
                }
            }
        }
        if (data_payments.size > 0) {
            for (i in data_payments.indices) {
                if (binding.mpesaRefSpinner.selectedItem.toString() == data_payments[i].ref
                ) {
                    mpesaAmount = data_payments[i].amount_paid
                }
            }
        }
        if (allocatedAmount > receiptAmount) {
            binding.logs.text =
                "Oops! Kindly enter amount below " + receiptAmount + " For receipt " + binding.receiptSpinner.selectedItem.toString()
            return
        }

        var allocatedMpesaRefTotal = 0
        var receiptAmountTotal = 0

        if (data_allocation.size > 0) {
            for (y in data_allocation.indices) {
                if (data_allocation[y].mpesa_ref == binding.mpesaRefSpinner.selectedItem.toString()) {
                    allocatedMpesaRefTotal += data_allocation[y].amount_allocated
                }
                if (data_allocation[y].receipt == binding.receiptSpinner.selectedItem.toString()) {
                    receiptAmountTotal += data_allocation[y].amount_allocated
                }
            }
        }
        if (receiptAmountTotal >= receiptAmount) {
            binding.logs.text =
                "Oops! " + binding.receiptSpinner.selectedItem.toString() + " receipt has been fully paid. Kindly select another receipt "
            Log.d(
                TAG,
                "Oops! " + binding.receiptSpinner.selectedItem.toString() + " receipt has been fully paid. Kindly select another receipt "
            )
            return
        }
        if (allocatedAmount > receiptAmount - receiptAmountTotal) {
            binding.logs.text =
                "Oops! Kindly enter amount below " + (receiptAmount - receiptAmountTotal) + " For receipt " + binding.receiptSpinner.selectedItem.toString()
            Log.d(
                TAG,
                "Oops! Kindly enter amount below " + (receiptAmount - receiptAmountTotal) + " For receipt " + binding.receiptSpinner.selectedItem.toString()
            )
            return
        }
        if (allocatedMpesaRefTotal >= mpesaAmount) {
            binding.logs.text =
                "Oops! " + binding.mpesaRefSpinner.selectedItem.toString() + " amount has depleted. Kindly use another payment transaction "
            Log.d(
                TAG,
                "Oops! " + binding.mpesaRefSpinner.selectedItem.toString() + " amount has depleted. Kindly use another payment transaction "
            )
            return
        }
        if (allocatedAmount > mpesaAmount) {
            binding.logs.text =
                "Oops! " + binding.mpesaRefSpinner.selectedItem.toString() + " amount is less than the allocated amount. Kindly enter a lower amount "
            Log.d(
                TAG,
                "Oops! " + binding.mpesaRefSpinner.selectedItem.toString() + " amount is less than the allocated amount. Kindly enter a lower amount "
            )
            return
        }
        mainViewModel.saveAllocation(
            binding.receiptSpinner.selectedItem.toString(),
            binding.mpesaRefSpinner.selectedItem.toString(), allocatedAmount
        )
    }

    private fun deleteAllocations() {
        this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Delete all amount allocated?")
                setPositiveButton(
                    R.string.delete
                ) { _, _ ->
                    mainViewModel.clearAllAllocation()
                }
                setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            }

            builder.create().show()
        }
    }
}