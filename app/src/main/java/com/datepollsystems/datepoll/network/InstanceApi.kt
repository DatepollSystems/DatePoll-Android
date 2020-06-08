package com.datepollsystems.datepoll.network

import com.datepollsystems.datepoll.data.*
import com.datepollsystems.datepoll.database.model.event.GetAllEventsResponseMsg
import com.datepollsystems.datepoll.database.model.event.VoteForEventRequestDto
import com.datepollsystems.datepoll.database.model.event.VoteForEventResponseDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Instance API
 * all http interfaces of the instance service
 */
interface InstanceApi {

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

    @POST("/api/v1/user/myself/changePassword/checkOldPassword")
    suspend fun checkOldPassword(
        @Query("token") token: String,
        @Body body: PasswordRequestModel
    ): Response<Message>

    @POST("/api/v1/user/myself/changePassword/changePassword")
    suspend fun changeOldPassword(
        @Query("token") token: String,
        @Body body: ChangePasswordRequestModel
    ): Response<Message>

    @GET("/api/v1/user/myself/token/calendar")
    suspend fun getCalendarToken(
        @Query("token") token: String
    ): Response<MessageToken>

    @DELETE("/api/v1/user/myself/token/calendar")
    suspend fun deleteCalendarToken(
        @Query("token") token: String
    ): Response<Message>


    @GET("/api/v1/avent")
    suspend fun getAllEvents(
        @Query("token") token: String
    ): Response<GetAllEventsResponseMsg>

    @POST("/api/v1/avent/vote")
    suspend fun voteForEvent(
        @Query("token") token: String,
        @Body vote: VoteForEventRequestDto
    ): Response<VoteForEventResponseDto>

    @DELETE("/api/v1/avent/vote/{eventId}")
    suspend fun removeVoteForEvent(
        @Path("eventId") id: Int,
        @Query("token") token: String
    ): Response<Message>


}