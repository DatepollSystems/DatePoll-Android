package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.data.FirstPasswdChangeRequest
import com.datepollsystems.datepoll.data.LoginRequestModel
import com.datepollsystems.datepoll.data.LoginResponseModel
import com.datepollsystems.datepoll.network.InstanceApi


class LoginRepository : BaseRepository() {

    suspend fun login(
        username: String,
        password: String,
        loginState: MutableLiveData<ENetworkState?>
    ): LoginResponseModel? {
        val requestObj =
            LoginRequestModel(username, password)
        val api: InstanceApi = getKoin().get()
        return apiCall(
            call = { api.login(requestObj) },
            state = loginState
        )
    }

    suspend fun setFirstPasswd(
        username: String,
        oldPasswd: String,
        newPasswd: String,
        state: MutableLiveData<ENetworkState?>
    ): LoginResponseModel? {
        val requestObj = FirstPasswdChangeRequest(
            username = username,
            newPassword = newPasswd,
            oldPassword = oldPasswd
        )
        val api: InstanceApi = getKoin().get()
        return apiCall(
            call = { api.changePasswordWhenSignIn(requestObj) },
            state = state
        )
    }
}
