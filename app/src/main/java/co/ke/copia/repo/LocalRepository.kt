package co.ke.copia.repo

import co.ke.copia.models.AllocationItem

class LocalRepository(
    private val receiptsDAO: ReceiptsDAO,
    private val transactionsDAO: TransactionsDAO,
    private val allocationDAO: AllocationDAO
) {

    val receipts = receiptsDAO.getAllReceipts()
    val transactions = transactionsDAO.getAllTransactions()
    val allocations = allocationDAO.getAllAllocations()


    suspend fun insertAllocation(allocations: AllocationItem): Long {
        return allocationDAO.insertAllocation(allocations)
    }

    suspend fun deleteAllAllocations(): Int {
        return allocationDAO.deleteAll()
    }
}