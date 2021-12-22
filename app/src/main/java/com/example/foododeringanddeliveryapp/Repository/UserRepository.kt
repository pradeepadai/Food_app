package com.joytekmotion.yemilicious.Repository

import com.example.tour_guide_nepal.ENTITY.User



import com.example.tour_guide_nepal.Response.LoginResponse
import com.joytekmotion.yemilicious.API.MyApiRequest
import com.joytekmotion.yemilicious.API.ServiceBuilder
import com.joytekmotion.yemilicious.API.UserAPI

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




