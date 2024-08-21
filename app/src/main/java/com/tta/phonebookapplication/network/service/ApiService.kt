package com.tta.phonebookapplication.network.service

import com.tta.phonebookapplication.network.model.ContactResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("getcontact.php")
    suspend fun fetchContactList(): Response<List<ContactResponse>>
}