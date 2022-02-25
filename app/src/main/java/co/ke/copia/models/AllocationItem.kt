package co.ke.copia.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "allocation")
data class AllocationItem(

    @NotNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @NotNull
    @ColumnInfo(name = "receipt")
    var receipt: String,

    @NotNull
    @ColumnInfo(name = "mpesa_ref")
    var mpesa_ref: String,

    @NotNull
    @ColumnInfo(name = "amount_allocated")
    var amount_allocated: Int,

    ) {
    fun getAmount(): String {
        return amount_allocated.toString()
    }

}

