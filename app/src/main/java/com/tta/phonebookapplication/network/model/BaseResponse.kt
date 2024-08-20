package com.tta.phonebookapplication.network.model

import com.google.gson.annotations.SerializedName

open class BaseResponse<T>(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T? = null
)
