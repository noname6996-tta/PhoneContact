package com.tta.phonebookapplication.domain.repository

import com.tta.phonebookapplication.domain.datasource.dao.ContactDao
import com.tta.phonebookapplication.domain.entity.ContactEntity
import com.tta.phonebookapplication.domain.mapper.asEntity
import com.tta.phonebookapplication.network.service.ApiClient
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val dao: ContactDao
) : ContactRepository {
    override suspend fun getAllList(): List<ContactEntity> {
        val response = dao.getAllContacts()
        return response.ifEmpty {
            apiClient.fetchContactList().body()?.data?.asEntity() ?: response
        }
    }

    override suspend fun insertContact(contactEntity: ContactEntity) = dao.insert(contactEntity)
    override suspend fun insertListContact(listContactEntities: List<ContactEntity>) =
        dao.insertAll(listContactEntities)

    override suspend fun deleteContactById(id: Int) = dao.deleteContactByID(id)
    override suspend fun getInfoContactById(id: Int) = dao.getContactByID(id)
    override suspend fun deleteAll() = dao.deleteAllContactCodes()
    override suspend fun resetAutoIncrement() = dao.resetAutoIncrement()
    override suspend fun doesContactExist(email: String, phone: String): Int =
        dao.doesContactExist(email, phone)
}