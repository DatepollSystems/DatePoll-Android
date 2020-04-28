package com.datepollsystems.datepoll.repos

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.Prefs
import com.datepollsystems.datepoll.network.DatepollApi
import com.datepollsystems.datepoll.data.RefreshTokenWithSessionRequest
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response
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
                Log.e(tag, response.toString())
                throw NetworkErrorException("Response wasn't successful")
            }
        } catch (e: Exception) {
            Log.e(tag, "$e ${e.message!!}")
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

        val jwtTime = prefs.jwtRenewalTime

        /**
         * Check if user is logged in because otherwise he won't need a jwt for a request
         * Check if JWT is older than 1 hour -> then renew JWT
         */
        if (isLoggedIn() && (Date().time - jwtTime) > 3600000) {
            Log.i(tag, "Try to refresh jwt")
            val request =
                RefreshTokenWithSessionRequest(sessionToken = prefs.session!!)
            val response = api.refreshTokenWithSession(request
            )
            if (!response.isSuccessful) {
                return false
            }

            prefs.jwtRenewalTime = Date().time
            prefs.jwt = response.body()?.token
            Log.i(tag, "Token refresh successful")
        }

        return true
    }

    private fun isLoggedIn(): Boolean {
        return prefs.isLoggedIn && prefs.jwtRenewalTime > 0
    }
}