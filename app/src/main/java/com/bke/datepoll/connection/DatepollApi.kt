package com.bke.datepoll.connection

import com.bke.datepoll.connection.model.LoginRequestModel
import com.bke.datepoll.connection.model.LoginResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by kaulex on 17.10.19
 */
interface DatepollApi {

    @GET("/")
    suspend fun checkIfServiceIsOnline(): Response<ResponseBody>

    @POST("/api/auth/signin")
    suspend fun login(@Body request: LoginRequestModel): Response<LoginResponseModel>

}