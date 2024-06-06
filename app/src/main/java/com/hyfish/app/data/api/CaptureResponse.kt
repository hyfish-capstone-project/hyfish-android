package com.hyfish.app.data.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CaptureResponse(
	val data: CaptureItem,
	val message: String,
	val status: Boolean
) : Parcelable

@Parcelize
data class CaptureItem(
	val result: String,
	val image: String,
	val createdAt: String,
	val rate: Int,
	val id: Int
) : Parcelable
