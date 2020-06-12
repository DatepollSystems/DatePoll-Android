package com.datepollsystems.datepoll.repos

import android.accounts.NetworkErrorException
import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.Prefs
import com.datepollsystems.datepoll.data.ErrorMsg
import com.datepollsystems.datepoll.data.RefreshTokenWithSessionRequest
import com.datepollsystems.datepoll.network.InstanceApi
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response
import timber.log.Timber
import java.util.*

enum class ENetworkState(
    var message: String = "no message",
    var messageCode: String = "no message code",
    var code: Int = 0
) {
    LOADING,
    DONE,
    ERROR;
}

open class BaseRepository : KoinComponent {

    protected val prefs: Prefs by inject()
    private val api: InstanceApi by inject()

    /**
     * generic Datepoll API method to invoke calls, for simple calls with general error handling
     * @param call: api call which should be invoked
     * @param state: LiveData which receives the current state of the request
     * @return response body as T or null if something went wrong
     */
    suspend fun <T : Any> apiCall(
        call: suspend () -> Response<T>,
        state: MutableLiveData<ENetworkState>
    ): T? {
        state.postValue(ENetworkState.LOADING)
        var response: Response<T>? = null
        try {
            if (!checkIfJwtIsValid()) {
                Timber.e("User not authenticated")
            }
            response = call.invoke()
            if (response.isSuccessful) {
                val s = ENetworkState.DONE
                s.apply {
                    code = response.code()
                    messageCode = "Successful"
                }
                state.postValue(ENetworkState.DONE)
            } else {
                throw NetworkErrorException("Error during request")
            }
        } catch (e: Exception) {
            Timber.e("$e ${e.message!!}")

            val s = ENetworkState.ERROR

            response?.let {
                withContext(Dispatchers.IO) {
                    val adapter = Moshi.Builder().build().adapter(ErrorMsg::class.java)
                    val body = it.errorBody()?.string()
                    Timber.i("Error body: $body")
                    val error = adapter.fromJson(body.toString())

                    s.apply {
                        code = it.code()
                        messageCode = error?.error_code.toString()
                        message = error?.msg.toString()
                    }
                }
            }

            state.postValue(s)
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
            Timber.i("Try to refresh jwt")
            val request =
                RefreshTokenWithSessionRequest(sessionToken = prefs.session!!)
            val response = api.refreshTokenWithSession(
                request
            )
            if (!response.isSuccessful) {
                return false
            }

            prefs.jwtRenewalTime = Date().time
            prefs.jwt = response.body()?.token
            Timber.i("Token refresh successful")
        }

        return true
    }

    private fun isLoggedIn(): Boolean {
        return prefs.isLoggedIn && prefs.jwtRenewalTime > 0
    }
}