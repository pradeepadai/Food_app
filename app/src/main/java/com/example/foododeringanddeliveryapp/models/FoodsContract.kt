package com.example.foododeringanddeliveryapp.models

object FoodsContract {
    internal const val COLLECTION_NAME = "foods"

    // Users fields
    object Fields {
        const val FOOD_NAME = "name"
        const val FOOD_PRICE = "price"
        const val FOOD_DESC = "description"

        //        const val FOOD_IMAGE = "image"
        const val FOOD_SELLER = "seller"
    }
}