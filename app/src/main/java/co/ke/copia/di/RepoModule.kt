package  co.ke.copia.di


import co.ke.copia.repo.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {


    @Provides
    @Singleton
    fun provideLocalRepository(localDataSource: LocalDataSource) =
        LocalRepositoryImpl(localDataSource) as LocalRepository

    @Provides
    @Singleton
    fun provideLocalDataSource(
        receiptsDAO: ReceiptsDAO,
        transactionsDAO: TransactionsDAO,
        allocationDAO: AllocationDAO
    ) = LocalDataSourceImpl(receiptsDAO, transactionsDAO, allocationDAO) as LocalDataSource

}


