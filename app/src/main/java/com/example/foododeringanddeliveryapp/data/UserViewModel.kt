package com.example.foododeringanddeliveryapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.example.foododeringanddeliveryapp.models.User
import com.example.foododeringanddeliveryapp.models.UsersContract

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val _userInfo by lazy { MutableLiveData<User>() }
    val userInfo: LiveData<User>
        get() = _userInfo

    private val _userInfoError by lazy { MutableLiveData<String>() }
    val userInfoError: LiveData<String>
        get() = _userInfoError

    fun getUserProfile(uid: String) {
        Firebase.firestore
            .collection(UsersContract.COLLECTION_NAME)
            .document(uid)
            .get()
            .addOnSuccessListener {
                _userInfo.postValue(it.toObject<User>())
            }
            .addOnFailureListener {
                _userInfoError.value = it.message
            }
    }
}