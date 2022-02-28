package co.ke.copia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem
import co.ke.copia.repo.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: LocalRepository) : ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()
    var allocations: LiveData<MutableList<AllocationItem>> = MutableLiveData()
    var receipts: LiveData<MutableList<ReceiptItem>> = MutableLiveData()
    var transactions: LiveData<MutableList<PaymentItem>> = MutableLiveData()

    val message: LiveData<Event<String>>
        get() = statusMessage


    private fun insertAllocation(allocationItem: AllocationItem) = viewModelScope.launch {
        repository.insertAllocation(allocationItem)
        statusMessage.value = Event("Allocation Inserted Successfully ")
    }


    fun getAllReceipts() {
        viewModelScope.launch {
            receipts = repository.getAllReceipts()
        }
    }

    fun getAllTransactions() {
        viewModelScope.launch {
            transactions = repository.getAllTransactions()
        }
    }

    fun getAllAllocations() {
        viewModelScope.launch {
            allocations = repository.getAllAllocations()
        }
    }

    fun saveAllocation(receipt: String, mpesa_ref: String, amount_allocated: Int) {

        insertAllocation(AllocationItem(0, receipt, mpesa_ref, amount_allocated))

    }

    fun clearAllAllocation() {
        clearAll()
    }

    private fun clearAll() = viewModelScope.launch {
        repository.deleteAllAllocations()
        statusMessage.value = Event("Allocations Deleted Successfully")

    }
}