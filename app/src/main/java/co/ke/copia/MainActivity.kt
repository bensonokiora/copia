package co.ke.copia

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import co.ke.copia.databinding.ActivityMainBinding
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var receiptList: MutableList<ReceiptItem>
    private lateinit var paymentsList: MutableList<PaymentItem>
    private lateinit var allocationList: MutableList<AllocationItem>


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

        binding.run.setOnClickListener {

            insertAllocation("R001", "MG001", 100)
            insertAllocation("R002", "MG002", 200)
            insertAllocation("R002", "MG003", 200)
            insertAllocation("R003", "MG003", 100)
            insertAllocation("R003", "MG004", 250)

        }

        binding.fabDeleteAll.setOnClickListener {
            deleteAllocations()
        }

    }

    private fun getReceipts(it: List<ReceiptItem>) {
        if (it.isNotEmpty()) {
            receiptList = java.util.ArrayList()
            receiptList.addAll(it)
        }
    }

    private fun getTransactions(it: List<PaymentItem>?) {
        if (it != null) {
            if (it.isNotEmpty()) {
                paymentsList = java.util.ArrayList()
                paymentsList.addAll(it)
            }
        }
    }

    private fun getAllocations(it: List<AllocationItem>) {
        allocationList = java.util.ArrayList()
        if (it.isNotEmpty()) {
            allocationList.addAll(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun insertAllocation(receipt: String, transaction: String, allocatedAmount: Int) {


         var receiptAmount = 0
         var mpesaAmount = 0

        if (TextUtils.isEmpty(allocatedAmount.toString())) {
            Log.d(TAG,"Oops! You haven't entered any allocation amount ")
            return
        }
        if (receiptList.size > 0) {
            for (i in receiptList.indices) {
                if (receipt == receiptList[i].receipt) {
                    receiptAmount = receiptList[i].amount_to_be_paid
                }
            }
        }
        if (paymentsList.size > 0) {
            for (i in paymentsList.indices) {
                if (transaction == paymentsList[i].ref) {
                    mpesaAmount = paymentsList[i].amount_paid
                }
            }
        }
        if (allocatedAmount > receiptAmount) {
            Log.d(TAG, "Oops! Kindly enter amount below $receiptAmount For receipt $receipt")
            return
        }

        var allocatedMpesaRefTotal = 0
        var receiptAmountTotal = 0

        if (allocationList.size > 0) {
            for (y in allocationList.indices) {
                if (allocationList[y].mpesa_ref == transaction) {
                    allocatedMpesaRefTotal += allocationList[y].amount_allocated
                }
                if (allocationList[y].receipt == receipt) {
                    receiptAmountTotal += allocationList[y].amount_allocated
                }
            }
        }
        if (receiptAmountTotal >= receiptAmount) {
            Log.d(TAG,"Oops! $receipt receipt has been fully paid. Kindly select another receipt "
            )
            return
        }
        if (allocatedAmount > receiptAmount - receiptAmountTotal) {
            Log.d(
                TAG,
                "Oops! Kindly enter amount below " + (receiptAmount - receiptAmountTotal) + " For receipt " + receipt
            )
            return
        }
        if (allocatedMpesaRefTotal >= mpesaAmount) {
            Log.d(TAG,"Oops! $transaction amount has depleted. Kindly use another payment transaction ")
            return
        }
        if (allocatedAmount > mpesaAmount) {
            Log.d(TAG,"Oops! $transaction amount is less than the allocated amount. Kindly enter a lower amount ")
            return
        }
        viewModel.saveAllocation(
            receipt,
            transaction, allocatedAmount
        )

        Log.d("Inserted", "Receipt: "+ receipt + "Mpesa Ref: "+ transaction+"Amount Allocated: "+allocatedAmount)
        //binding.logs.text ="Receipt: "+ receipt + "Mpesa Ref: "+ transaction+"Amount Allocated: "+allocatedAmount

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