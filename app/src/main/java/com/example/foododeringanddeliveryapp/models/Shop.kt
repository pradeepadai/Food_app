package com.example.foododeringanddeliveryapp.models

data class Shop(
    val name: String,
    val address: String,
) {
    constructor() : this("", "")
}