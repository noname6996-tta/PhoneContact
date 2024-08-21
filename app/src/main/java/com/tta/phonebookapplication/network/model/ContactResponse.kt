package com.tta.phonebookapplication.network.model

import com.google.gson.annotations.SerializedName

data class ContactResponse(
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phone")
    var phone: String,
)
