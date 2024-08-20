package com.tta.phonebookapplication.domain.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tta.phonebookapplication.utils.TABLE_NAME
import com.tta.phonebookapplication.domain.datasource.dao.ContactDao
import com.tta.phonebookapplication.domain.entity.ContactEntity

@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        private var instance: AppDatabase? = null

        /*
        The purpose of this code is to ensure that only one instance of AppDatabase is created in the application,
        even if multiple threads access the getDatabase method at the same time.
        It makes use of synchronized to avoid race conditions and instance ?: to check
        and initialize the instance if necessary.
         */
        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, TABLE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}