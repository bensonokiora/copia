package co.ke.copia.repo

import androidx.room.Dao
import androidx.room.Query
import co.ke.copia.models.PaymentItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDAO {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<PaymentItem>>
}