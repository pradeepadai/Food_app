package com.joytekmotion.yemilicious.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joytekmotion.yemilicious.models.Shop
import com.joytekmotion.yemilicious.models.ShopsContract

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