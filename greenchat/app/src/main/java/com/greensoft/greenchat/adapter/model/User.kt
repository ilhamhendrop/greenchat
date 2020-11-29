package com.greensoft.greenchat.adapter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var uid: String?,
    var username: String?,
    var photo: String?,
    ): Parcelable {
    constructor() : this("", "", "")
}