package com.example.foododeringanddeliveryapp.API


import com.example.foododeringanddeliveryapp.Response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {
    //register user
    @POST("registration/insert")
    suspend fun registerUser(
        @Body user: com.example.foododeringanddeliveryapp.ENTITY.User
    ): Response<LoginResponse>

    //login user
    @FormUrlEncoded
    @POST("user/login")
    suspend fun checkUser(
        @Field("email") email:String,
        @Field("password") password:String
    ):Response<LoginResponse>
}