package com.example.foododeringanddeliveryapp.Repository

import com.example.foododeringanddeliveryapp.API.MyApiRequest
import com.example.foododeringanddeliveryapp.API.ServiceBuilder
import com.example.foododeringanddeliveryapp.API.UserAPI
import com.example.foododeringanddeliveryapp.ENTITY.User
import com.example.foododeringanddeliveryapp.Response.LoginResponse


class UserRepository:  MyApiRequest() {
    private val userAPI = ServiceBuilder.buildService(UserAPI::class.java)

    //Register User
    suspend fun registerUser(user: User) : LoginResponse {
        return apiRequest {
            userAPI.registerUser(user)
        }
    }

    //login User
    suspend fun loginUser(email:String, password:String):LoginResponse{
        return apiRequest {
            userAPI.checkUser(email, password)
        }
    }

}




