package com.example.foododeringanddeliveryapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.foododeringanddeliveryapp.models.Shop
import com.example.foododeringanddeliveryapp.models.ShopsContract

class ShopViewModel(application: Application) : AndroidViewModel(application) {

//    fun addShop(shop: Shop) {
//        val currentUser = Firebase.auth.currentUser
//        if (currentUser != null) {
//            val db = Firebase.firestore
//
//            db.collection(ShopsContract.COLLECTION_NAME).document
//        }
//    }
}