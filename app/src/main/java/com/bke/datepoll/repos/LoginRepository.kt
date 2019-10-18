package com.bke.datepoll.repos

import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.connection.model.LoginRequestModel
import com.bke.datepoll.connection.model.LoginResponseModel
import okhttp3.ResponseBody


class LoginRepository(private val api : DatepollApi) : BaseRepository(){

    suspend fun isServiceOnline(): ResponseBody? {
        return safeApiCall(
            call = {api.checkIfServiceIsOnline()},
            errorMessage = "Error Fetching Popular Movies"
        )
    }

    suspend fun login(username: String, password: String): LoginResponseModel? {
        val requestObj = LoginRequestModel(username, password)

        return safeApiCall(
            call = { api.login(requestObj)},
            errorMessage = "Could not sign in"
        )
    }
}
