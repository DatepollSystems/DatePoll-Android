package com.datepollsystems.datepoll.network

import com.datepollsystems.datepoll.core.Prefs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
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

        Timber.i("Performing requests on url: $url")

        val retrofit = Retrofit.Builder()
            .client(datepollClient)
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(InstanceApi::class.java)
    }

    fun createDatepollService(): DatepollApi {

        val url = "https://releases.datepoll.org/DatePoll/"

        val datepollClient = OkHttpClient().newBuilder()
            .hostnameVerifier(HostnameVerifier { _, session ->
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                hv.verify("releases.datepoll.org", session)
            }).build()

        val retrofit = Retrofit.Builder()
            .client(datepollClient)
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(DatepollApi::class.java)
    }
}