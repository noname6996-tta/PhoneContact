package com.tta.phonebookapplication.data.repository

import com.tta.phonebookapplication.data.model.Contact

interface ContactRepository {
    suspend fun getAllList(): List<Contact>
    suspend fun insertContact(contact: Contact): Unit
    suspend fun insertListContact(listContacts: List<Contact>): Unit
    suspend fun deleteContactById(id: Int): Unit
    suspend fun getInfoContactById(id: Int): Contact
    suspend fun deleteAll(): Unit
    suspend fun resetAutoIncrement(): Unit
    suspend fun doesContactExist(email: String, phone: String ): Int
}