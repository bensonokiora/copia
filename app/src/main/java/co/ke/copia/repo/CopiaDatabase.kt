package co.ke.copia.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem

@Database(entities = [ReceiptItem::class, AllocationItem::class, PaymentItem::class], exportSchema = false, version = 1)
abstract class CopiaDatabase : RoomDatabase() {
    abstract val receiptsDAO: ReceiptsDAO
    abstract val transactionsDAO: TransactionsDAO
    abstract val allocationDAO: AllocationDAO

    companion object {
        @Volatile
        private var INSTANCE: CopiaDatabase? = null
        fun getInstance(context: Context): CopiaDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CopiaDatabase::class.java,
                        "copia.db",
                    ).createFromAsset("database/copia.db")
                        .build()
                }
                return instance
            }
        }
    }
}

