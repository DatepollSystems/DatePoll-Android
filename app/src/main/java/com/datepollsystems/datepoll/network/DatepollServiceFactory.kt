package com.datepollsystems.datepoll.network

import com.datepollsystems.datepoll.core.Prefs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

object DatepollServiceFactory {

    fun createInstanceService(prefs: Prefs): InstanceApi {

        val url = "${prefs.serverAddress}:${prefs.serverPort}"

        val datepollClient = OkHttpClient().newBuilder()
            .hostnameVerifier(HostnameVerifier { _, session ->
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                hv.verify(prefs.serverAddress?.removePrefix("https://"), session)
            }).build()


        val retrofit = Retrofit.Builder()
            .client(datepollClient)
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(InstanceApi::class.java)
    }

    fun createDatepollService(): DatepollApi {

        val url = "https://datepoll.org:443"

        val datepollClient = OkHttpClient().newBuilder()
            .hostnameVerifier(HostnameVerifier { _, session ->
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                hv.verify("datepoll.org", session)
            }).build()


        val retrofit = Retrofit.Builder()
            .client(datepollClient)
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(DatepollApi::class.java)
    }
}