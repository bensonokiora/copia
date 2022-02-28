package co.ke.copia.repo

import androidx.lifecycle.LiveData
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem

interface LocalRepository {

    suspend fun insertAllocation(allocation: AllocationItem)

    suspend fun deleteAllAllocations()

    suspend fun getAllReceipts(): LiveData<MutableList<ReceiptItem>>

    suspend fun getAllTransactions(): LiveData<MutableList<PaymentItem>>

    suspend fun getAllAllocations(): LiveData<MutableList<AllocationItem>>

}