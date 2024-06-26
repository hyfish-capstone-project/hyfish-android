package com.hyfish.app.data.api.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("data") val data: LoginData,

    @field:SerializedName("message") val message: String,

    @field:SerializedName("status") val status: Boolean
) : Parcelable

@Parcelize
data class LoginData(

    @field:SerializedName("token") val token: String,

    @field:SerializedName("role") val role: String,
) : Parcelable
