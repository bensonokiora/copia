package co.ke.copia

import androidx.lifecycle.*
import co.ke.copia.models.AllocationItem
import co.ke.copia.repo.LocalRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val repository: LocalRepository) : ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


    fun saveAllocation(receipt: String, mpesa_ref: String, amount_allocated: Int) {
        insertAllocation(AllocationItem(0, receipt, mpesa_ref, amount_allocated))

    }

    private fun insertAllocation(allocationItem: AllocationItem) = viewModelScope.launch {
        val newRowId = repository.insertAllocation(allocationItem)
        if (newRowId > -1) {
            statusMessage.value = Event("Allocation Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }


    fun getReceipts() = liveData {
        repository.receipts.collect {
            emit(it)
        }
    }

    fun getTransactions() = liveData {
        repository.transactions.collect {
            emit(it)
        }
    }

    fun getAllocations() = liveData {
        repository.allocations.collect {
            emit(it)
        }
    }

    fun clearAllAllocation() {
        clearAll()
    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAllAllocations()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Allocations Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
}