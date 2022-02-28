package co.ke.copia.repo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.ke.copia.models.AllocationItem

@Dao
interface AllocationDAO {

    @Insert
    suspend fun insertAllocation(allocationItem: AllocationItem): Long

    @Query("SELECT * FROM allocation")
    fun getAllAllocations(): LiveData<MutableList<AllocationItem>>

    @Query("DELETE FROM allocation")
    suspend fun deleteAll(): Int
}