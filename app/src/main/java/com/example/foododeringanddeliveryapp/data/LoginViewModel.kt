package com.example.foododeringanddeliveryapp.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foododeringanddeliveryapp.models.User
import com.example.foododeringanddeliveryapp.models.UsersContract
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

private const val TAG = "RegisterUsersViewModel"

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = Firebase.auth

    private val _currentUserInfo by lazy { MutableLiveData<User>() }
    val currentUserInfo: LiveData<User>
        get() = _currentUserInfo

    private val _currentUser by lazy { MutableLiveData<FirebaseUser>() }
    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    private val _loginError by lazy { MutableLiveData<String>() }
    val loginError: LiveData<String>
        get() = _loginError

    private val _userRole by lazy { MutableLiveData<String?>() }
    val userRole: LiveData<String?>
        get() = _userRole

    init {
        loggedInUser()
        loggedInUserInfo()
    }

    fun login(username: String, password: String) {
        Log.d(TAG, "login called!")
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "login: signInWithEmail success")
                    _currentUser.value = auth.currentUser
                } else {
                    Log.d(TAG, "login: signInWithEmail success")
//                    _loginError.value = getApplication<Application>().getString(R.string.error)
                }
            }
    }

    private fun loggedInUser() {
        Log.d(TAG, "loggedInUser called")
        _currentUser.postValue(auth.currentUser)
        Log.d(TAG, "loggedInUser: return authenticated user")
    }

    private fun loggedInUserInfo() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Firebase.firestore
                .collection(UsersContract.COLLECTION_NAME)
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener {
                    _currentUserInfo.postValue(it.toObject<User>())
                }
        }
    }

    fun checkSetup(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val db = Firebase.firestore
            db.collection(UsersContract.COLLECTION_NAME)
                .document(currentUser.uid)
                .get().addOnSuccessListener {
                    if (it.exists()) {
                        _userRole.value = it.toObject<User>()?.role
                    } else _userRole.value = null
                }.addOnFailureListener {
                    _loginError.value = it.message
                    Log.d(TAG, "Exception >> ${it.message}")
                }
        }
    }

    fun logout() {
        Log.d(TAG, "logout called!")
        Firebase.auth.signOut()
        _currentUser.postValue(auth.currentUser)
        Log.d(TAG, "logout: User logged out successfully")
    }
}