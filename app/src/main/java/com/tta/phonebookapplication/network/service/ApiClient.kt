package com.tta.phonebookapplication.network.service

import com.tta.phonebookapplication.network.model.ContactResponse
import retrofit2.Response
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchContactList(): Response<List<ContactResponse>> {
        return apiService.fetchContactList()
    }
}