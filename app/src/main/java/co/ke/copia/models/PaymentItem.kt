package co.ke.copia.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "transactions")
data class PaymentItem(

    @NotNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @NotNull
    @ColumnInfo(name = "ref")
    var ref: String,

    @NotNull
    @ColumnInfo(name = "amount_paid")
    var amount_paid: Int,


    ) {

    override fun toString(): String {
        return ref
    }

}


