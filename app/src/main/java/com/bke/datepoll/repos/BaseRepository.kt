package com.bke.datepoll.repos

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bke.datepoll.Prefs
import com.bke.datepoll.network.DatepollApi
import com.bke.datepoll.network.Result
import com.bke.datepoll.data.requests.ErrorMsg
import com.bke.datepoll.data.requests.RefreshTokenWithSessionRequest
import com.bke.datepoll.exceptions.AuthorizationFailedException
import com.squareup.moshi.Moshi
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

enum class ENetworkState { LOADING, DONE, ERROR }

open class BaseRepository(private val tag: String) : KoinComponent {

    protected val prefs: Prefs by inject()
    private val api: DatepollApi by inject()

    /**
     * abstract Datepoll API method to invoke calls
     * @param call: api call which should be invoked
     * @param state: LiveData which receives the current state of the request
     * @return response body or null if something went wrong
     */
    suspend fun <T : Any> apiCall(
        call: suspend () -> Response<T>,
        state: MutableLiveData<ENetworkState>
    ): T? {

        state.postValue(ENetworkState.LOADING)
        var response: Response<T>? = null
        try {
            if(!checkIfJwtIsValid()){
                Log.e(tag, "User not authenticated")
            }
            response = call.invoke()
            if (response.isSuccessful) {
                state.postValue(ENetworkState.DONE)
            } else {
                throw NetworkErrorException("")
            }
        } catch (e: Exception) {
            Log.e(tag, e.message!!)
            state.postValue(ENetworkState.ERROR)
        }

        return response?.body()
    }

    /**
     * Checks if current JWT is still valid
     * if not try to renew it
     * @return true if JWT is valid
     */
    private suspend fun checkIfJwtIsValid(): Boolean {

        val jwtTime = prefs.JWT_RENEWAL_TIME

        /**
         * Check if user is logged in because otherwise he won't need a jwt for a request
         * Check if JWT is older than 1 hour -> then renew JWT
         */
        if (isLoggedIn() && (Date().time - jwtTime) > 3600000) {
            Log.i(tag, "Try to refresh jwt")
            val request = RefreshTokenWithSessionRequest(session_token = prefs.SESSION!!)
            val response = api.refreshTokenWithSession(request)

            if (!response.isSuccessful) {
                return false
            }

            prefs.JWT_RENEWAL_TIME = Date().time
            prefs.JWT = response.body()?.token
            Log.i(tag, "Token refresh successful")
        }

        return true
    }

    private fun isLoggedIn(): Boolean {
        return prefs.IS_LOGGED_IN && prefs.JWT_RENEWAL_TIME > 0
    }
}