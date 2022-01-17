package com.example.foododeringanddeliveryapp.data

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.models.*

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val _isFoodAdded by lazy { MutableLiveData<Boolean>() }
    val isFoodAdded: LiveData<Boolean>
        get() = _isFoodAdded

    private val _validationErrors by lazy { MutableLiveData<HashMap<String, String>>() }
    val validationErrors: LiveData<HashMap<String, String>>
        get() = _validationErrors

    private val _foodAddError by lazy { MutableLiveData<String>() }
    val foodAddError: LiveData<String>
        get() = _foodAddError

    private val _foods by lazy { MutableLiveData<ArrayList<Food>>() }
    val foods: LiveData<ArrayList<Food>>
        get() = _foods

    private val _foodsErrors by lazy { MutableLiveData<String>() }
    val foodsErrors: LiveData<String>
        get() = _foodsErrors

    private val _food by lazy { MutableLiveData<Food>() }
    val food: LiveData<Food>
        get() = _food

    private val _imageURL by lazy { MutableLiveData<Uri>() }
    val imageURL: LiveData<Uri>
        get() = _imageURL

    private val _foodDeleteSuccess by lazy { MutableLiveData<String>() }
    val foodDeleteSuccess: LiveData<String>
        get() = _foodDeleteSuccess

    private val _foodDeleteError by lazy { MutableLiveData<String>() }
    val foodDeleteError: LiveData<String>
        get() = _foodDeleteError

    private val currentUser = Firebase.auth.currentUser

    fun addFood(food: Food) {
        if (!isValidateFoodErrors(food)) {
            if (currentUser != null) {
                if (!isValidateFoodErrors(food))
                    if (food.id == "")
                        food.id = Firebase.firestore.collection(FoodsContract.COLLECTION_NAME)
                            .document().id
                Firebase.firestore
                    .collection(FoodsContract.COLLECTION_NAME).document(food.id).set(food)
                    .addOnSuccessListener {
                        _isFoodAdded.value = true
                    }.addOnFailureListener {
                        _foodAddError.value = it.message
                    }
            }
        }
    }

    fun getFoods() {
        if (currentUser != null) {
            Firebase.firestore
                .collection(FoodsContract.COLLECTION_NAME)
                .whereEqualTo(FieldPath.of("seller", "uid"), currentUser.uid)
                .get()
                .addOnSuccessListener {
                    _foods.postValue(it.toObjects<Food>() as ArrayList<Food>)
                }.addOnFailureListener {
                    _foodsErrors.value = it.message
                }
        }
    }

    fun getFoodImageURL(imageName: String) {
        val pathReference = Firebase.storage.reference
            .child(imageName)
        pathReference.downloadUrl.addOnSuccessListener {
            _imageURL.value = it
        }
    }

    fun deleteFood(food: Food) {
        Firebase.firestore.collection(FoodsContract.COLLECTION_NAME).document(food.id)
            .delete().addOnSuccessListener {
                deleteFoodImage(food)
                getFoods()
            }.addOnFailureListener {
                _foodDeleteError.value = it.message
            }
    }

    private fun deleteFoodImage(food: Food) {
        Firebase.storage.reference.child(food.image!!).delete().addOnFailureListener {
            _foodDeleteError.value = it.message
        }.addOnSuccessListener {
            _foodDeleteSuccess.value = "${food.name} removed successfully!"
        }
    }

    private fun isValidateFoodErrors(food: Food): Boolean {
        val validationErrors = HashMap<String, String>()
        // Validate food name
        if (food.name.isBlank()) {
            validationErrors[FoodsContract.Fields.FOOD_NAME] =
                getApplication<Application>().getString(R.string.food_name_required)
        }
        // Validate food price
        if (food.price > 0.0) {
            if (food.price.isNaN()) {
                validationErrors[FoodsContract.Fields.FOOD_PRICE] =
                    getApplication<Application>().getString(R.string.invalid_food_price)
            }
        } else validationErrors[FoodsContract.Fields.FOOD_PRICE] =
            getApplication<Application>().getString(R.string.food_price_required)
        // Validate food description
        if (food.description.isBlank()) {
            validationErrors[FoodsContract.Fields.FOOD_DESC] =
                getApplication<Application>().getString(R.string.food_desc_required)
        }

        // Send validation set errors to live data
        if (!validationErrors.isNullOrEmpty()) {
            _validationErrors.value = validationErrors
            return true
        }
        return false
    }

}