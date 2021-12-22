package com.joytekmotion.yemilicious.API

import com.example.tour_guide_nepal.ENTITY.User
import com.example.tour_guide_nepal.Response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {
    //register user
    @POST("registration/insert")
    suspend fun registerUser(
        @Body user: User
    ): Response<LoginResponse>

    //login user
    @FormUrlEncoded
    @POST("user/login")
    suspend fun checkUser(
        @Field("email") email:String,
        @Field("password") password:String
    ):Response<LoginResponse>
}