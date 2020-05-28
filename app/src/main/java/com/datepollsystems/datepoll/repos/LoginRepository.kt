package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.LoginRequestModel
import com.datepollsystems.datepoll.data.LoginResponseModel
import com.datepollsystems.datepoll.network.DatepollApi
import okhttp3.ResponseBody
import org.koin.core.inject


class LoginRepository : BaseRepository("LoginRepository") {

    private val api: DatepollApi by inject()

    suspend fun isServiceOnline(): ResponseBody? {
        return apiCall(
            call = { api.checkIfServiceIsOnline() },
            state = MutableLiveData()
        )
    }

    suspend fun login(username: String, password: String): LoginResponseModel? {
        val requestObj =
            LoginRequestModel(username, password)

        return apiCall(
            call = { api.login(requestObj) },
            state = MutableLiveData()
        )
    }
}
