package com.sanjeet.innobuz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostItem(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
): Parcelable