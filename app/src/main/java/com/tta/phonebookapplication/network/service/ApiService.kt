package com.tta.phonebookapplication.network.service

import com.tta.phonebookapplication.network.model.BaseResponse
import com.tta.phonebookapplication.network.model.ContactResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("")
    suspend fun fetchContactList(): Response<BaseResponse<List<ContactResponse>>>
}