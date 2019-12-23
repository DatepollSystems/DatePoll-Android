package com.bke.datepoll.connection

import com.bke.datepoll.data.requests.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * DatepollAPI
 * all http interfaces of the Datepoll service
 */
interface DatepollApi {

    /**
     * Checking if the server is running and ready for work
     */
    @GET("/")
    suspend fun checkIfServiceIsOnline(): Response<ResponseBody>


    /**
     * Authorization stuff for getting a jwt token or renewing it with a session token
     */
    @POST("/api/auth/signin")
    suspend fun login(@Body request: LoginRequestModel): Response<LoginResponseModel>

    @POST("/api/auth/IamLoggedIn")
    suspend fun refreshTokenWithSession(@Body request:RefreshTokenWithSessionRequest): Response<RefreshTokenWithSessionResponse>

    @POST("/api/v1/user/myself/session/logoutCurrentSession")
    suspend fun logout(@Body requestModel: LogoutRequestModel): Response<LogoutResponseModel>

    /**
     * General requests for getting user information
     */
    @GET("/api/v1/user/myself")
    suspend fun currentUser(@Query("token") token: String) : Response<CurrentUserResponseModel>





}