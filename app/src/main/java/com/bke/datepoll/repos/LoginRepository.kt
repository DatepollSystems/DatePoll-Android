package com.bke.datepoll.repos

import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.data.requests.LoginRequestModel
import com.bke.datepoll.data.requests.LoginResponseModel
import okhttp3.ResponseBody


class LoginRepository(private val api : DatepollApi) : BaseRepository("LoginRepository"){

    suspend fun isServiceOnline(): ResponseBody? {
        return safeApiCall(
            api,
            call = {api.checkIfServiceIsOnline()},
            errorMessage = "Service could not be reached"
        )
    }

    suspend fun login(username: String, password: String): LoginResponseModel? {
        val requestObj = LoginRequestModel(username, password)

        return safeApiCall(
            api,
            call = { api.login(requestObj)},
            errorMessage = "Could not sign in"
        )
    }
}
