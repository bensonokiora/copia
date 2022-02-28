package co.ke.copia.repo.sources

import androidx.lifecycle.LiveData
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem
import co.ke.copia.repo.data.AllocationDAO
import co.ke.copia.repo.data.ReceiptsDAO
import co.ke.copia.repo.data.TransactionsDAO
import javax.inject.Inject

interface LocalDataSource {
    fun getReceipts(): LiveData<MutableList<ReceiptItem>>

    fun getTransactions(): LiveData<MutableList<PaymentItem>>

    fun getAllocations(): LiveData<MutableList<AllocationItem>>

    suspend fun cacheAllocation(data: AllocationItem) {}

    suspend fun deleteAllocation() {}

}


class LocalDataSourceImpl @Inject constructor(
    private val receiptsDAO: ReceiptsDAO,
    private val transactionsDAO: TransactionsDAO,
    private val allocationDAO: AllocationDAO
) :
    LocalDataSource {

    override fun getReceipts(): LiveData<MutableList<ReceiptItem>> = receiptsDAO.getAllReceipts()

    override fun getTransactions(): LiveData<MutableList<PaymentItem>> =
        transactionsDAO.getAllTransactions()

    override fun getAllocations(): LiveData<MutableList<AllocationItem>> =
        allocationDAO.getAllAllocations()

    override suspend fun cacheAllocation(data: AllocationItem) {
        allocationDAO.insertAllocation(data)
    }

    override suspend fun deleteAllocation() {
        allocationDAO.deleteAll()
    }


}