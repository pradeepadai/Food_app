package com.example.foododeringanddeliveryapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foododeringanddeliveryapp.models.UsersContract
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _role: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val role: LiveData<String>
        get() = _role

    private val _updateFailed: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val updateFailed: LiveData<String>
        get() = _updateFailed

    fun updateProfile(user: User) {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val db = Firebase.firestore
            val usersCollection = db.collection(UsersContract.COLLECTION_NAME)
            usersCollection
                    .document(currentUser.uid)
                    .set(user)
                    .addOnSuccessListener {
                        _role.value = user.role
                    }
                    .addOnFailureListener {
                        _updateFailed.value = "${it.message}"
                    }
        }
    }

//    fun updateShop(shop: Shop) {
//        val currentUser = Firebase.auth.currentUser
//        if (currentUser != null) {
//            val db = Firebase.firestore
//            val shopsCollection = db.collection(ShopsContract.COLLECTION_NAME)
//            shopsCollection
//                    .document(currentUser.uid)
//                    .set(shop)
//                    .addOnSuccessListener {
//                        _updateSuccess.value = getApplication<Application>().getString(R.string.shop_update_success)
//                    }
//                    .addOnFailureListener {
//                        _updateFailed.value = "${it.message}"
//                    }
//        }
//    }
}