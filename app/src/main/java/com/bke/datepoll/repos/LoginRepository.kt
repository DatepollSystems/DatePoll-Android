package com.bke.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.network.DatepollApi
import com.bke.datepoll.data.LoginRequestModel
import com.bke.datepoll.data.LoginResponseModel
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
