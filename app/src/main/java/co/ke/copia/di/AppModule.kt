package  co.ke.copia.di

import android.content.Context
import androidx.room.Room
import co.ke.copia.repo.CopiaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        CopiaDatabase::class.java, "copia.db"
    )
        .createFromAsset("database/copia.db")
        .build()

    @Singleton
    @Provides
    fun provideReceiptsDAO(db: CopiaDatabase) = db.getReceiptsDAO()

    @Singleton
    @Provides
    fun provideTransactionsDAO(db: CopiaDatabase) = db.getTransactionsDAO()

    @Singleton
    @Provides
    fun provideAllocationDAO(db: CopiaDatabase) = db.getAllocationDAO()


}





