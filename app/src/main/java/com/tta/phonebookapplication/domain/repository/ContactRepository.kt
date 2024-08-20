package com.tta.phonebookapplication.domain.repository

import com.tta.phonebookapplication.domain.entity.ContactEntity

interface ContactRepository {
    suspend fun getAllList(): List<ContactEntity>
    suspend fun insertContact(contactEntity: ContactEntity): Unit
    suspend fun insertListContact(listContactEntities: List<ContactEntity>): Unit
    suspend fun deleteContactById(id: Int): Unit
    suspend fun getInfoContactById(id: Int): ContactEntity
    suspend fun deleteAll(): Unit
    suspend fun resetAutoIncrement(): Unit
    suspend fun doesContactExist(email: String, phone: String ): Int
}