package com.example.foododeringanddeliveryapp.models

object OrdersContract {
    internal const val COLLECTION_NAME = "orders"

    // Users fields
    object Fields {
        const val ORDER_STATUS = "status"
    }

    object Responses {
        const val REJECTED = "rejected"
        const val PENDING = "pending"
        const val PROCESSING = "processing"
        const val DELIVERED = "delivered"
//        const val FAILED = "failed"
    }
}