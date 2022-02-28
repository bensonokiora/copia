package co.ke.copia

import androidx.lifecycle.LiveData
import co.ke.copia.models.AllocationItem
import co.ke.copia.models.PaymentItem
import co.ke.copia.models.ReceiptItem
import co.ke.copia.repo.sources.LocalDataSource
import co.ke.copia.repo.LocalRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is
import org.junit.Assert.assertThat
import org.junit.Test


class CopiaLocalRepositoryTest {


val fakeList=  mutableListOf<AllocationItem>().apply {
        add(
            AllocationItem(
            id = 1,
            receipt = "R001",
            mpesa_ref = "MG001",
            amount_allocated = 100
        )
        )
        add(AllocationItem(
            id = 2,
            receipt = "R002",
            mpesa_ref = "MG002",
            amount_allocated = 200
        ))

    }


    @Test
    fun getCopiaSources_andInsertit_inRoom(){

        runBlocking {
            val localDataSource = object : LocalDataSource {
                override fun getReceipts(): LiveData<MutableList<ReceiptItem>> {
                    TODO("Not yet implemented")
                }

                override fun getTransactions(): LiveData<MutableList<PaymentItem>> {
                    TODO("Not yet implemented")
                }

                override fun getAllocations(): LiveData<MutableList<AllocationItem>> {
                    TODO("Not yet implemented")
                }
            }
            val localRepository = LocalRepositoryImpl(localDataSource)

            val result = localRepository.getAllAllocations()
            val exepected = listOf(
                AllocationItem(
                    id = 1,
                    receipt = "R001",
                    mpesa_ref = "MG001",
                    amount_allocated = 100
                ),
                AllocationItem(
                    id = 2,
                    receipt = "R002",
                    mpesa_ref = "MG002",
                    amount_allocated = 200
                )
            )
            assertThat(result, Is.`is`(exepected))
        }
    }


}

