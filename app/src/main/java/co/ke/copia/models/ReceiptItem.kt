package co.ke.copia.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "receipts")
data class ReceiptItem(

    @NotNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @NotNull
    @ColumnInfo(name = "receipt")
    var receipt: String,

    @NotNull
    @ColumnInfo(name = "amount_to_be_paid")
    var amount_to_be_paid: Int,


    ) {
    override fun toString(): String {
        return receipt
    }
}

