package com.tta.phonebookapplication.network.model

import com.google.gson.annotations.SerializedName

data class ContactResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phone")
    var phone: String,
)
