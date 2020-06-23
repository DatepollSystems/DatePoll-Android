package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.data.FirstPasswdChangeRequest
import com.datepollsystems.datepoll.data.LoginRequestModel
import com.datepollsystems.datepoll.data.LoginResponseModel
import com.datepollsystems.datepoll.network.InstanceApi
import okhttp3.ResponseBody
import org.koin.core.inject


class LoginRepository : BaseRepository() {

    private val api: InstanceApi by inject()

    suspend fun login(
        username: String,
        password: String,
        loginState: MutableLiveData<ENetworkState>
    ): LoginResponseModel? {
        val requestObj =
            LoginRequestModel(username, password)

        return apiCall(
            call = { api.login(requestObj) },
            state = loginState
        )
    }

    suspend fun setFirstPasswd(
        username: String,
        oldPasswd: String,
        newPasswd: String,
        state: MutableLiveData<ENetworkState>
    ): LoginResponseModel? {
        val requestObj = FirstPasswdChangeRequest(
            username = username,
            newPassword = newPasswd,
            oldPassword = oldPasswd
        )

        return apiCall(
            call = { api.changePasswordWhenSignIn(requestObj) },
            state = state
        )
    }
}
