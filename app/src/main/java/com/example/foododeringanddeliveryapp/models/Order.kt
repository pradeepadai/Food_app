package com.joytekmotion.yemilicious.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp


data class Order(
    val food: Food,
    val buyer: User,
    val seller: User?,
    val status: String,
) {
    constructor() : this(Food(), User(), User(), "")

    var id: String = ""

    @ServerTimestamp
    var timestamp: Timestamp? = null
}