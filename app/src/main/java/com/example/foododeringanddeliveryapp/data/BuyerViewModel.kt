package com.joytekmotion.yemilicious.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.joytekmotion.yemilicious.models.Food
import com.joytekmotion.yemilicious.models.FoodsContract
import com.joytekmotion.yemilicious.models.User
import com.joytekmotion.yemilicious.models.UsersContract
import com.joytekmotion.yemilicious.ui.SELLER_ROLE

class BuyerViewModel(application: Application) : AndroidViewModel(application) {

    private val _users by lazy { MutableLiveData<ArrayList<User>>() }
    val users: LiveData<ArrayList<User>>
        get() = _users

    private val _usersErrors by lazy { MutableLiveData<String>() }
    val usersErrors: LiveData<String>
        get() = _usersErrors

    private val _sellersFoods by lazy { MutableLiveData<ArrayList<Food>>() }
    val sellersFoods: LiveData<ArrayList<Food>>
        get() = _sellersFoods

    private val _sellersFoodsError by lazy { MutableLiveData<String>() }
    val sellersFoodsError: LiveData<String>
        get() = _sellersFoodsError


    fun getSellers() {
        val db = Firebase.firestore

        db.collection(UsersContract.COLLECTION_NAME)
            .whereEqualTo(UsersContract.Fields.ROLE, SELLER_ROLE)
            .get()
            .addOnSuccessListener {
                _users.postValue(it.toObjects<User>() as ArrayList<User>)
            }
            .addOnFailureListener {
                _usersErrors.value = it.message
            }
    }

    fun getSellerFoods(uid: String) {
        Firebase.firestore.collection(FoodsContract.COLLECTION_NAME)
            .whereEqualTo(
                FieldPath.of(
                    FoodsContract.Fields.FOOD_SELLER,
                    UsersContract.Fields.UID
                ), uid
            )
            .get().addOnSuccessListener {
                _sellersFoods.postValue(it.toObjects<Food>() as ArrayList<Food>)
            }.addOnFailureListener {
                _sellersFoodsError.value = it.message
            }
    }
}