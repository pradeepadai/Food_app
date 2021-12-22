package com.example.foododeringanddeliveryapp.models

object UsersContract {
    internal const val COLLECTION_NAME = "users"

    // Users fields
    object Fields {
        const val NAME = "name"
        const val EMAIL = "email"
        const val Phone = "phone"
        const val PASSWORD = "password"
        const val PASSWORD_CONFIRM = "password_confirm"
        const val ROLE = "role"
        const val UID = "uid"
    }
}