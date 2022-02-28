package co.ke.copia.repo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import co.ke.copia.models.PaymentItem

@Dao
interface TransactionsDAO {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): LiveData<MutableList<PaymentItem>>
}