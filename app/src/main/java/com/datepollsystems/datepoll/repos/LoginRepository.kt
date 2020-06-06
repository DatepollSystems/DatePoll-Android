package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.LoginRequestModel
import com.datepollsystems.datepoll.data.LoginResponseModel
import com.datepollsystems.datepoll.network.InstanceApi
import okhttp3.ResponseBody
import org.koin.core.inject


class LoginRepository : BaseRepository("LoginRepository") {

    private val api: InstanceApi by inject()

    suspend fun login(username: String, password: String, loginState: MutableLiveData<ENetworkState>): LoginResponseModel? {
        val requestObj =
            LoginRequestModel(username, password)

        return apiCall(
            call = { api.login(requestObj) },
            state = loginState
        )
    }
}
