package com.hyfish.app.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FishResponse(

    @field:SerializedName("data") val data: List<FishItem>,

    @field:SerializedName("message") val message: String,

    @field:SerializedName("status") val status: Boolean
) : Parcelable

@Parcelize
data class FishItem(

    @field:SerializedName("id") val id: Int,

    @field:SerializedName("images") val images: List<String>,

    @field:SerializedName("name") val name: String,

    @field:SerializedName("description") val description: String,

    @field:SerializedName("created_at") val createdAt: String,

    @field:SerializedName("nutrition_image_url") val nutritionImageUrl: String? = null,
) : Parcelable
