package com.tta.phonebookapplication.domain.mapper

import com.tta.phonebookapplication.domain.entity.ContactEntity
import com.tta.phonebookapplication.network.model.ContactResponse

object ContactMapper : EntityMapper<List<ContactResponse>, List<ContactEntity>> {
    override fun asEntity(domain: List<ContactResponse>): List<ContactEntity> {
        // from api to database
        return domain.map { contact ->
            ContactEntity(
                id = null,
                name = contact.name,
                email = contact.email,
                phone = contact.phone
            )
        }
    }

    override fun asDomain(entity: List<ContactEntity>): List<ContactResponse> {
        // from database to api
        return entity.map { contact ->
            ContactResponse(
                id = contact.id ?: 0,
                name = contact.name,
                email = contact.email,
                phone = contact.phone
            )
        }
    }
}

fun List<ContactResponse>.asEntity(): List<ContactEntity> {
    return ContactMapper.asEntity(this)
}

fun List<ContactEntity>.asDomain(): List<ContactResponse> {
    return ContactMapper.asDomain(this)
}