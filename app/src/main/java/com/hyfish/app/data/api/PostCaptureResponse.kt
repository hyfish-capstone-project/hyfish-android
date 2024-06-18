package com.hyfish.app.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostCaptureResponse(

    @field:SerializedName("data") val data: CaptureItem,

    @field:SerializedName("message") val message: String,

    @field:SerializedName("status") val status: Boolean
) : Parcelable

@Parcelize
data class CaptureItem(

    @field:SerializedName("score") val score: Double,

    @field:SerializedName("updated_at") val updatedAt: String,

    @field:SerializedName("user_id") val userId: Int,

    @field:SerializedName("image_url") val imageUrl: String,

    @field:SerializedName("created_at") val createdAt: String,

    @field:SerializedName("id") val id: Int,

    @field:SerializedName("type") val type: String,

    @field:SerializedName("freshness") val freshness: String? = null,

    @field:SerializedName("fish_id") val fishId: Int? = null,
) : Parcelable

@Parcelize
data class CaptureItemWithFish(

    @field:SerializedName("score") val score: Double,

    @field:SerializedName("updated_at") val updatedAt: String,

    @field:SerializedName("user_id") val userId: Int,

    @field:SerializedName("image_url") val imageUrl: String,

    @field:SerializedName("created_at") val createdAt: String,

    @field:SerializedName("id") val id: Int,

    @field:SerializedName("type") val type: String,

    @field:SerializedName("freshness") val freshness: String? = null,

    @field:SerializedName("fish_id") val fishId: Int? = null,

    @field:SerializedName("fish") val fish: FishItem? = null,
) : Parcelable