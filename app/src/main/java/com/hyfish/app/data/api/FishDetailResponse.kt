package com.hyfish.app.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FishDetailResponse(

    @field:SerializedName("data") val data: Data? = null,

    @field:SerializedName("message") val message: String? = null,

    @field:SerializedName("status") val status: Boolean? = null
) : Parcelable

@Parcelize
data class RecipesItem(

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("ingredients") val ingredients: List<IngredientsItem?>? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("steps") val steps: List<StepsItem?>? = null
) : Parcelable

@Parcelize
data class Data(

    @field:SerializedName("recipes") val recipes: List<RecipesItem?>? = null,

    @field:SerializedName("images") val images: List<String?>? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("description") val description: String? = null,

    @field:SerializedName("created_at") val createdAt: String? = null,

    @field:SerializedName("id") val id: Int? = null
) : Parcelable

@Parcelize
data class StepsItem(

    @field:SerializedName("description") val description: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("order") val order: Int? = null
) : Parcelable

@Parcelize
data class IngredientsItem(

    @field:SerializedName("amount") val amount: Int? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("measurement") val measurement: String? = null
) : Parcelable
