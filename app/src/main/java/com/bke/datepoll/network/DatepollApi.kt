package com.bke.datepoll.network

import com.bke.datepoll.data.model.*
import com.bke.datepoll.data.requests.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Datepoll API
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
    suspend fun login(
        @Body request: LoginRequestModel
    ): Response<LoginResponseModel>


    @POST("/api/auth/IamLoggedIn")
    suspend fun refreshTokenWithSession(
        @Body request: RefreshTokenWithSessionRequest
    ): Response<RefreshTokenWithSessionResponse>


    @POST("/api/v1/user/myself/session/logoutCurrentSession")
    suspend fun logout(
        @Query("token") token: String,
        @Body requestModel: LogoutRequestModel
    ): Response<LogoutResponseModel>

    /**
     * General requests for getting user information
     */
    @GET("/api/v1/user/myself")
    suspend fun currentUser(
        @Query("token") token: String
    ): Response<CurrentUserResponseModel>


    @PUT("/api/v1/user/myself")
    suspend fun updateCurrentUser(
        @Query("token") token: String,
        @Body u: UpdateUserRequest
    ): Response<CurrentUserResponseModel>


    /**
     * Home screen
     */
    @GET("/api/v1/user/homepage")
    suspend fun getHomepage(
        @Query("token") token: String
    ): Response<HomeScreen>


    @POST("/api/v1/avent/vote")
    suspend fun voteForEvent(
        @Query("token") token: String
    ): Response<VoteResponse>

    /**
     * Settings screen
     */
    @POST("/api/v1/user/myself/phoneNumber")
    suspend fun addPhoneNumber(
        @Query("token") token: String,
        @Body newNumber: NewPhoneNumberRequest
    ): Response<NewPhoneNumberResponse>

    @DELETE("/api/v1/user/myself/phoneNumber/{id}")
    suspend fun removePhoneNumber(
        @Path("id") id: Int,
        @Query("token") token: String
    ): Response<ResponseBody>

    @POST("/api/v1/user/myself/changeEmailAddresses")
    suspend fun addEmail(
        @Query("token") token: String,
        @Body body: AddEmailRequest
    ): Response<CurrentUserResponseModel>

    @GET("/api/v1/user/myself/session")
    suspend fun getSessions(
        @Query("token") token: String
    ): Response<Session>

    @DELETE("/api/v1/user/myself/session/{id}")
    suspend fun deleteSession(
        @Path("id") id: Int,
        @Query("token") token: String
    ): Response<Message>

}