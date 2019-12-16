package com.bke.datepoll.repos

import android.util.Log
import com.bke.datepoll.Prefs
import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.connection.Result
import com.bke.datepoll.data.requests.ErrorMsg
import com.bke.datepoll.data.requests.RefreshTokenWithSessionRequest
import com.bke.datepoll.error.AuthorizationFailedException
import com.squareup.moshi.Moshi
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*


open class BaseRepository(private val tag: String) : KoinComponent{

    protected val prefs: Prefs by inject()

    suspend fun <T : Any> safeApiCall(api:DatepollApi, call: suspend () -> Response<T>, errorMessage: String): T? {

        // Check if JWT is older than 1 hour -> if yes than renew, else -> Throw error
        val jwtTime = prefs.JWT_RENEWAL_TIME
        if(prefs.IS_LOGGED_IN && jwtTime > 0){
            val diff = Date().time - jwtTime
            if(diff > 3600000){
                val request = RefreshTokenWithSessionRequest(session_token = prefs.SESSION!!)
                val response = api.refreshTokenWithSession(request)
                if(!response.isSuccessful){
                    //TODO maybe error livedata -> Notify over snackbar
                    return null
                } else {
                    prefs.JWT_RENEWAL_TIME = Date().time
                    prefs.JWT = response.body()?.token
                    Log.i(tag, "Token refresh successful")
                }
            }
        }

        val result : Result<T> = safeApiResult(api, call,errorMessage)
        var data : T? = null

        when(result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.d("BaseRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }

        return data
    }

    private suspend fun <T: Any> safeApiResult(api: DatepollApi ,call: suspend ()-> Response<T>, errorMessage: String) : Result<T>{
        try {
            val response = call.invoke()

            if(response.isSuccessful)
                return Result.Success(response.body()!!)
            else {
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(ErrorMsg::class.java)
                val errorBody = response.errorBody()?.string()
                Log.e("code", response.code().toString())
                if (errorBody!!.isNotBlank()) {
                    Log.e("ErrorResponse", errorBody)
                    val errorMsg: ErrorMsg = adapter.fromJson(errorBody)!!
                    Log.e("Error during Request", errorMsg.msg)

                    if (response.code() == 401 && errorMsg.error_code == "token_incorrect") {
                        Log.e("Authorization failed", "try to get a new token")
                        try {
                            val jwtRefreshRequest = RefreshTokenWithSessionRequest(prefs.SESSION!!)
                            val newjwt = safeApiCall(
                                api,
                                call = { api.refreshTokenWithSession(jwtRefreshRequest) },
                                errorMessage = "Could not refresh the jwt token"
                            )

                            if (newjwt != null) {
                                prefs.JWT = newjwt.token
                                Log.i("Refreshed jwt token", newjwt.msg)

                                //retry original request



                            } else {
                                throw AuthorizationFailedException("Could not renew jwt token")
                            }
                        } catch (e: Exception) {
                            Log.e(tag, e.message!!)
                        }
                    }
                }
            }
        } catch (e: SocketTimeoutException){
            //TODO Handle
        } catch (e: UnknownHostException){

        } catch (e: HttpException){

        }


        return Result.Error(IOException("Error Occurred during request - $errorMessage"))
    }
}