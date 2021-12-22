package com.joytekmotion.yemilicious.data

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.joytekmotion.yemilicious.R
import com.joytekmotion.yemilicious.models.User
import com.joytekmotion.yemilicious.models.UsersContract

private const val TAG = "RegisterUsersViewModel"

class RegisterUsersViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentUser: MutableLiveData<FirebaseUser> by lazy { MutableLiveData<FirebaseUser>() }
    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    private val _loginError: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val loginError: LiveData<String>
        get() = _loginError

    private val _validationErrors: MutableLiveData<ContentValues> by lazy { MutableLiveData<ContentValues>() }
    val validationErrors: LiveData<ContentValues>
        get() = _validationErrors

    // Register a new user
    fun register(user: User, passwordConfirm: String) {
        // Check if user details provided in form is valid perform registration
        if (validateUserDetails(user, passwordConfirm)) {
            val auth = Firebase.auth

            Log.d(TAG, "register: Authentication STARTS")
            auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result?.user?.updateProfile(userProfileChangeRequest {
                            displayName = user.name
                        })
                        _currentUser.value = it.result?.user
                        // User profile created and authenticated successfully
                        Log.d(TAG, "register: User authentication created successfully!")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d(TAG, "register: createUserWithEmail:error", it.exception)
                        _loginError.value = it.exception?.message
                    }
                    }.addOnFailureListener {
                        // If sign in fails, display a message to the user.
                        Log.d(TAG, "register: createUserWithEmail:failure", it)
                        _loginError.value = it.message
                    }
        }
    }

    private fun validateUserDetails(user: User, passwordConfirm: String): Boolean {
        val errorMessages = ContentValues()
        val minPasswordLength = 6
        var isValid = true

        if (user.name.isBlank()) {
            errorMessages.put(
                    UsersContract.Fields.NAME,
                    getApplication<Application>().getString(R.string.name_required)
            )
            isValid = false
            Log.d(TAG, "validateUserDetails: Name is required!")
        }

        if (user.email.isBlank()) {
            errorMessages.put(UsersContract.Fields.EMAIL, getApplication<Application>().getString(R.string.email_address_required))
            isValid = false
            Log.d(TAG, "validateUserDetails: Email Address is required!")
        }

        when {
            user.password.isBlank() -> {
                errorMessages.put(UsersContract.Fields.PASSWORD, getApplication<Application>().getString(R.string.password_required))
                isValid = false
                Log.d(TAG, "validateUserDetails: Password is required!")
            }
            user.password.count() < minPasswordLength -> {
                errorMessages.put(UsersContract.Fields.PASSWORD, getApplication<Application>().getString(R.string.password_length, minPasswordLength))
                isValid = false
                Log.d(TAG, "validateUserDetails: Password must be greater than $minPasswordLength!")
            }
            user.password != passwordConfirm -> {
                errorMessages.put(UsersContract.Fields.PASSWORD, getApplication<Application>().getString(R.string.password_no_match))
                isValid = false
                Log.d(TAG, "Password do not match!")

            }
        }
        if (passwordConfirm.isBlank()) {
            errorMessages.put(UsersContract.Fields.PASSWORD_CONFIRM, getApplication<Application>().getString(R.string.password_retype_required))
            isValid = false
            Log.d(TAG, "Password Retype is required!")
        }

        _validationErrors.value = errorMessages
        return isValid
    }

}