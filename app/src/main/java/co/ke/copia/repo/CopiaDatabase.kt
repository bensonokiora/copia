package co.ke.copia.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem

@Database(
    entities = [ReceiptItem::class, AllocationItem::class, PaymentItem::class],
    exportSchema = false,
    version = 1
)
abstract class CopiaDatabase : RoomDatabase() {
    abstract fun getReceiptsDAO(): ReceiptsDAO
    abstract fun getTransactionsDAO(): TransactionsDAO
    abstract fun getAllocationDAO(): AllocationDAO
}

