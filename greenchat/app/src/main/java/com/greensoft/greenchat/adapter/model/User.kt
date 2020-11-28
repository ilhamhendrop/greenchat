package com.greensoft.greenchat.adapter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class User (
    var uid: String?,
    var username: String?,
    var photo: String?,
    ) {
    constructor() : this("", "", "")
}