package co.ke.copia.repo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import co.ke.copia.models.ReceiptItem

@Dao
interface ReceiptsDAO {

    @Query("SELECT * FROM receipts")
    fun getAllReceipts(): LiveData<MutableList<ReceiptItem>>
}