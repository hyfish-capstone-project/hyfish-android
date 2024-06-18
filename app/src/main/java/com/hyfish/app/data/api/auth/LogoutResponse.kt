package com.hyfish.app.data.api.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LogoutResponse(

    @field:SerializedName("message") val message: String,

    @field:SerializedName("status") val status: Boolean
) : Parcelable
