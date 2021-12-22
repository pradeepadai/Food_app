package com.joytekmotion.yemilicious.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    val name: String,
    val price: Double,
    val description: String,
    var image: String?,
    var seller: User?,
    var id: String = ""
) : Parcelable {
    constructor() : this("", 0.0, "", null, User(), "")
    constructor(name: String, price: Double, description: String, image: String?, seller: User?)
            : this(name, price, description, image, seller, "")
}