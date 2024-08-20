package com.tta.phonebookapplication.domain.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tta.phonebookapplication.utils.TABLE_NAME
import com.tta.phonebookapplication.domain.datasource.base.BaseDao
import com.tta.phonebookapplication.domain.entity.ContactEntity

@Dao
interface ContactDao : BaseDao<ContactEntity> {
    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllContacts(): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contactEntities: List<ContactEntity>)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    suspend fun deleteContactByID(id: Int)

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getContactByID(id: Int): ContactEntity

    @Query("DELETE FROM $TABLE_NAME ")
    suspend fun deleteAllContactCodes()

    // Reset id (Primary key) count from 1
    @Query("DELETE FROM sqlite_sequence WHERE name = '$TABLE_NAME'")
    suspend fun resetAutoIncrement()

    // if have data same email and phone return > 0
    @Query("SELECT COUNT(*) FROM $TABLE_NAME WHERE email = :email AND phone = :phone")
    suspend fun doesContactExist(email: String, phone: String): Int
}