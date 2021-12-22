package com.joytekmotion.yemilicious.models

data class Shop(
    val name: String,
    val address: String,
) {
    constructor() : this("", "")
}