package com.tta.phonebookapplication.data.repository

import com.tta.phonebookapplication.data.datasource.dao.ContactDao
import com.tta.phonebookapplication.data.model.Contact
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val dao: ContactDao
) : ContactRepository {
    override suspend fun getAllList(): List<Contact> = dao.getAllContacts()
    override suspend fun insertContact(contact: Contact) = dao.insert(contact)
    override suspend fun insertListContact(listContacts: List<Contact>) = dao.insertAll(listContacts)
    override suspend fun deleteContactById(id: Int) = dao.deleteContactByID(id)
    override suspend fun getInfoContactById(id: Int) = dao.getContactByID(id)
    override suspend fun deleteAll() = dao.deleteAllContactCodes()
    override suspend fun resetAutoIncrement() = dao.resetAutoIncrement()
    override suspend fun doesContactExist(email: String, phone: String): Int = dao.doesContactExist(email, phone)
}