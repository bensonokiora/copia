package co.ke.copia.repo

import androidx.room.Dao
import androidx.room.Query
import co.ke.copia.models.ReceiptItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptsDAO {

    @Query("SELECT * FROM receipts")
    fun getAllReceipts(): Flow<List<ReceiptItem>>
}