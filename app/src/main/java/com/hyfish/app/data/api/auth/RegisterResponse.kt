package com.hyfish.app.data.api.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(

    @field:SerializedName("data") val data: RegisterData
) : Parcelable

@Parcelize
data class RegisterData(

    @field:SerializedName("id") val id: Int,

    @field:SerializedName("email") val email: String,

    @field:SerializedName("username") val username: String
) : Parcelable
