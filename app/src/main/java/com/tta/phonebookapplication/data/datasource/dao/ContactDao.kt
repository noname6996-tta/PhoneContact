package com.tta.phonebookapplication.data.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tta.phonebookapplication.utils.TABLE_NAME
import com.tta.phonebookapplication.data.datasource.base.BaseDao
import com.tta.phonebookapplication.data.model.Contact

@Dao
interface ContactDao : BaseDao<Contact> {
    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllContacts(): List<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<Contact>)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    suspend fun deleteContactByID(id: Int)

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getContactByID(id: Int): Contact

    @Query("DELETE FROM $TABLE_NAME ")
    suspend fun deleteAllContactCodes()

    // Reset id (Primary key) count from 1
    @Query("DELETE FROM sqlite_sequence WHERE name = '$TABLE_NAME'")
    suspend fun resetAutoIncrement()

    // if have data same email and phone return > 0
    @Query("SELECT COUNT(*) FROM $TABLE_NAME WHERE email = :email AND phone = :phone")
    suspend fun doesContactExist(email: String, phone: String): Int
}