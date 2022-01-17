package com.example.foododeringanddeliveryapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.example.foododeringanddeliveryapp.R
import com.example.foododeringanddeliveryapp.models.Order
import com.example.foododeringanddeliveryapp.models.OrdersContract

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val _orderSuccess by lazy { MutableLiveData<String>() }
    val orderSuccess: LiveData<String>
        get() = _orderSuccess

    private val _orderError by lazy { MutableLiveData<String>() }
    val orderError: LiveData<String>
        get() = _orderError

    private val _cancelSuccess by lazy { MutableLiveData<String>() }
    val cancelSuccess: LiveData<String>
        get() = _cancelSuccess

    private val _cancelError by lazy { MutableLiveData<String>() }
    val cancelError: LiveData<String>
        get() = _cancelError

    private val _buyerOrders by lazy { MutableLiveData<ArrayList<Order>>() }
    val buyerOrders: LiveData<ArrayList<Order>>
        get() = _buyerOrders

    private val _buyerOrdersError by lazy { MutableLiveData<String>() }
    val buyerOrderError: LiveData<String>
        get() = _buyerOrdersError

    private val _sellerOrders by lazy { MutableLiveData<ArrayList<Order>>() }
    val sellerOrders: LiveData<ArrayList<Order>>
        get() = _sellerOrders

    private val _sellerOrdersError by lazy { MutableLiveData<String>() }
    val sellerOrderError: LiveData<String>
        get() = _sellerOrdersError

    private val _orderStatusUpdate by lazy { MutableLiveData<String>() }
    val orderStatusUpdate: LiveData<String>
        get() = _orderStatusUpdate

    private val _orderStatusError by lazy { MutableLiveData<String>() }
    val orderStatusError: LiveData<String>
        get() = _orderStatusError

    fun makeOrder(order: Order) {
        order.id = Firebase.firestore.collection(OrdersContract.COLLECTION_NAME).document().id
        Firebase.firestore
            .collection(OrdersContract.COLLECTION_NAME).document(order.id).set(order)
            .addOnSuccessListener {
                _orderSuccess.value =
                    getApplication<Application>().getString(R.string.order_completed_success)
            }.addOnFailureListener {
                _orderError.value = it.message
            }
    }

    fun getBuyerOrders(uid: String) {
        Firebase.firestore.collection(OrdersContract.COLLECTION_NAME)
            .whereEqualTo("buyer.uid", uid).get()
            .addOnSuccessListener {
                _buyerOrders.postValue(it.toObjects<Order>() as ArrayList<Order>)
            }
            .addOnFailureListener {
                _buyerOrdersError.value = it.message
            }
    }

    fun removeOrder(order: Order) {
        Firebase.firestore.collection(OrdersContract.COLLECTION_NAME)
            .document(order.id).delete()
            .addOnSuccessListener {
                _cancelSuccess.value =
                    getApplication<Application>().getString(R.string.order_cancelled_success)
                getBuyerOrders(order.buyer.uid)
            }.addOnFailureListener {
                _cancelError.value = it.message
            }
    }

    fun getSellerOrders(uid: String) {
        Firebase.firestore.collection(OrdersContract.COLLECTION_NAME)
            .whereEqualTo("seller.uid", uid).get()
            .addOnSuccessListener {
                _sellerOrders.postValue(it.toObjects<Order>() as ArrayList<Order>)
            }
            .addOnFailureListener {
                _sellerOrdersError.value = it.message
            }
    }

    fun updateOrderStatus(order: Order, status: String) {
        Firebase.firestore.collection(OrdersContract.COLLECTION_NAME)
            .document(order.id)
            .update(OrdersContract.Fields.ORDER_STATUS, status)
            .addOnSuccessListener {
                _orderStatusUpdate.value =
                    getApplication<Application>().getString(R.string.order_status_updated_success)
            }
            .addOnFailureListener {
                _orderStatusError.value = it.message
            }
    }

}