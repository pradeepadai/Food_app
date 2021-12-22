package com.example.foododeringanddeliveryapp.models

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var phone: String? = null,
    var address: String? = null,
    var uid: String = ""
) : Parcelable {
    constructor() : this(null, null, "")
    constructor(phone: String?, address: String?) : this(phone, address, "")

    @IgnoredOnParcel
    var role: String = ""

    @IgnoredOnParcel
    var shop: Shop? = null

    @IgnoredOnParcel
    var email: String = ""

    @IgnoredOnParcel
    var name: String = ""

    @IgnoredOnParcel
    @get:Exclude
    var password: String = ""
}