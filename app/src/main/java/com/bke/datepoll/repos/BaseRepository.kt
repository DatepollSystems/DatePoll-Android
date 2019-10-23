package com.bke.datepoll.repos

import android.util.Log
import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.connection.Result
import com.bke.datepoll.data.requests.ErrorMsg
import com.bke.datepoll.data.requests.RefreshTokenWithSessionRequest
import com.bke.datepoll.exceptions.AuthorizationFailedException
import com.bke.datepoll.prefs
import com.squareup.moshi.Moshi
import retrofit2.Response
import java.io.IOException


open class BaseRepository{

    suspend fun <T : Any> safeApiCall(api:DatepollApi, call: suspend () -> Response<T>, errorMessage: String): T? {

        val result : Result<T> = safeApiResult(api, call,errorMessage)
        var data : T? = null

        when(result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }

        return data

    }

    private suspend fun <T: Any> safeApiResult(api: DatepollApi ,call: suspend ()-> Response<T>, errorMessage: String) : Result<T>{
        val response = call.invoke()
        if(response.isSuccessful) return Result.Success(response.body()!!)
        else {

            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(ErrorMsg::class.java)
            val errorBody = response.errorBody().toString()

            if(errorBody.isNotBlank()){
                Log.e("ErrorRepsonse", errorBody)
                val errorMsg: ErrorMsg = adapter.fromJson(errorBody)!!
                Log.e("Error during Request", errorMsg.msg)

                if(response.code() == 401){
                    Log.e("Authorization failed", "try to get a new token")

                    val jwtRefreshRequest = RefreshTokenWithSessionRequest(prefs.SESSION!!)
                    val newjwt = safeApiCall(
                        api,
                        call= { api.refreshTokenWithSession(jwtRefreshRequest) },
                        errorMessage = "Could not refresh the jwt token"
                    )

                    if(newjwt != null) {
                        prefs.JWT = newjwt.token
                        Log.i("Refreshed jwt token", newjwt.msg)
                    } else {
                        throw AuthorizationFailedException("Could not renew jwt token")
                    }
                }
            }
        }

        return Result.Error(IOException("Error Occurred during request - $errorMessage"))
    }
}