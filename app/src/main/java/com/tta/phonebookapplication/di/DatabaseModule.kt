package com.tta.phonebookapplication.di

import android.content.Context
import com.tta.phonebookapplication.domain.datasource.dao.ContactDao
import com.tta.phonebookapplication.domain.datasource.db.AppDatabase
import com.tta.phonebookapplication.domain.repository.ContactRepository
import com.tta.phonebookapplication.domain.repository.ContactRepositoryImpl
import com.tta.phonebookapplication.network.service.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
        dao: ContactDao,
        apiClient: ApiClient
    ): ContactRepository {
        return ContactRepositoryImpl(apiClient, dao)
    }
}
