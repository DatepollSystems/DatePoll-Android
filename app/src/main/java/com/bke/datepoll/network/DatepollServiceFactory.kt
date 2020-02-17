package com.bke.datepoll.network

import com.bke.datepoll.Prefs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

object DatepollServiceFactory {

    fun createDatepollService(prefs: Prefs) : DatepollApi {

        val url = "${prefs.SERVER_ADDRESS}:${prefs.SERVER_PORT}"

        val datepollClient = OkHttpClient().newBuilder()
            .hostnameVerifier(HostnameVerifier { _, session ->
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                hv.verify(prefs.SERVER_ADDRESS?.removePrefix("https://"), session)
            }).build()


        val retrofit = Retrofit.Builder()
            .client(datepollClient)
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(DatepollApi::class.java)
    }
}