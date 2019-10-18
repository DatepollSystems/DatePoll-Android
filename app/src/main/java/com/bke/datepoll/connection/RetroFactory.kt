package com.bke.datepoll.connection

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetroFactory {

    private val datepollClient = OkHttpClient().newBuilder()
        .build()

    fun createDatepollService() : DatepollApi {
        val retrofit = Retrofit.Builder()
            .client(datepollClient)
            .baseUrl("https://datepoll-testing.dafnik.me:8443")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(DatepollApi::class.java)
    }
}