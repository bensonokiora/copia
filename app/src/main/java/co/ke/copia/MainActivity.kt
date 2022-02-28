package co.ke.copia

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import co.ke.copia.adapter.AllocationsAdapter
import co.ke.copia.databinding.ActivityMainBinding
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var receiptList: ArrayList<ReceiptItem>
    private lateinit var paymentsList: ArrayList<PaymentItem>
    private lateinit var allocationList: ArrayList<AllocationItem>

    private var receiptAmount: Int = 0
    private var mpesaAmount: Int = 0
    private var allocatedAmount: Int = 0

    private val TAG = MainActivity::class.java.simpleName


    private val viewModel: MainViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        viewModel.getAllAllocations()
        viewModel.getAllReceipts()
        viewModel.getAllTransactions()


        viewModel.receipts.observe(this) {
            getReceipts(it)
        }

        viewModel.transactions.observe(this) {
            getTransactions(it)
        }

        viewModel.allocations.observe(this) {
            getAllocations(it)
        }
        viewModel.message.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        binding.save.setOnClickListener {
            insertAllocation()
        }

        binding.fabDeleteAll.setOnClickListener {
            deleteAllocations()
        }

    }

    private fun getReceipts(it: List<ReceiptItem>) {
        if (it.isNotEmpty()) {
            receiptList = java.util.ArrayList()
            receiptList.addAll(it)
            val adapter = ArrayAdapter(
                this, android.R.layout.simple_spinner_item,
                receiptList
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.receiptSpinner.adapter = adapter
            Log.d(TAG, "Adding data to spinner Receipts")
        }
    }

    private fun getTransactions(it: List<PaymentItem>?) {
        if (it != null) {
            if (it.isNotEmpty()) {
                paymentsList = java.util.ArrayList()
                paymentsList.addAll(it)
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item,
                    paymentsList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.mpesaRefSpinner.adapter = adapter
                Log.d(TAG, "Adding data to spinner Mpesa Transactions")
            }
        }
    }

    private fun getAllocations(it: List<AllocationItem>) {
        allocationList = java.util.ArrayList()
        binding.allocationLayout.allocationParent.visibility = View.GONE
        if (it.isNotEmpty()) {
            binding.allocationLayout.allocationParent.visibility = View.VISIBLE
            allocationList.addAll(it)

            val adapter = AllocationsAdapter(allocationList)
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
        if (receiptList.size > 0) {
            for (i in receiptList.indices) {
                if (binding.receiptSpinner.selectedItem.toString() == receiptList[i].receipt
                ) {
                    receiptAmount = receiptList[i].amount_to_be_paid
                }
            }
        }
        if (paymentsList.size > 0) {
            for (i in paymentsList.indices) {
                if (binding.mpesaRefSpinner.selectedItem.toString() == paymentsList[i].ref
                ) {
                    mpesaAmount = paymentsList[i].amount_paid
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

        if (allocationList.size > 0) {
            for (y in allocationList.indices) {
                if (allocationList[y].mpesa_ref == binding.mpesaRefSpinner.selectedItem.toString()) {
                    allocatedMpesaRefTotal += allocationList[y].amount_allocated
                }
                if (allocationList[y].receipt == binding.receiptSpinner.selectedItem.toString()) {
                    receiptAmountTotal += allocationList[y].amount_allocated
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
        viewModel.saveAllocation(
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
                    viewModel.clearAllAllocation()
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