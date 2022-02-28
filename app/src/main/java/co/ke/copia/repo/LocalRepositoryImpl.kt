package co.ke.copia.repo

import androidx.lifecycle.LiveData
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : LocalRepository {


    private suspend fun cacheAllocation(data: AllocationItem) =
        localDataSource.cacheAllocation(data)

    private fun getReceipts(): LiveData<MutableList<ReceiptItem>> = localDataSource.getReceipts()
    private fun getTransactions(): LiveData<MutableList<PaymentItem>> =
        localDataSource.getTransactions()

    private fun getAllocations(): LiveData<MutableList<AllocationItem>> =
        localDataSource.getAllocations()

    private suspend fun deleteAllocations() = localDataSource.deleteAllocation()


    override suspend fun insertAllocation(allocation: AllocationItem) {
        cacheAllocation(allocation)
    }

    override suspend fun deleteAllAllocations() {
        deleteAllocations()
    }

    override suspend fun getAllReceipts(): LiveData<MutableList<ReceiptItem>> {
        return getReceipts()
    }

    override suspend fun getAllTransactions(): LiveData<MutableList<PaymentItem>> {
        return getTransactions()
    }

    override suspend fun getAllAllocations(): LiveData<MutableList<AllocationItem>> {
        return getAllocations()
    }

}