package com.datepollsystems.datepoll.network

import com.datepollsystems.datepoll.data.Instances
import retrofit2.Response
import retrofit2.http.GET


/**
 * Datepoll API
 * all http interfaces of the datepoll service
 */
interface DatepollApi {
    @GET("instances.json")
    suspend fun getInstances(): Response<Instances>
}