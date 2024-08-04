package com.tta.phonebookapplication.di

import android.content.Context
import com.tta.phonebookapplication.data.datasource.dao.ContactDao
import com.tta.phonebookapplication.data.datasource.db.AppDatabase
import com.tta.phonebookapplication.data.repository.ContactRepository
import com.tta.phonebookapplication.data.repository.ContactRepositoryImpl
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideContactDao(db: AppDatabase): ContactDao {
        return db.contactDao()
    }

    @Provides
    @Singleton
    fun provideRepository(
        dao: ContactDao
    ) : ContactRepository {
        return ContactRepositoryImpl(dao)
    }
}
