package com.datepollsystems.datepoll

import com.datepollsystems.datepoll.network.InstanceApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.net.HttpURLConnection

class RetrofitIntegrationTest {

    private lateinit var server: MockWebServer
    private lateinit var retrofit: Retrofit
    private lateinit var service: InstanceApi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .baseUrl(server.url("/"))
            .build()

        service = retrofit
            .create(InstanceApi::class.java)
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun testApiInfoCall() = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(
                    "{ 'version': '0.8.0','version_number': 15,'application_url': 'testing.datepoll.org', 'community_name': 'Datepoll Demo', 'community_url': 'https://datepoll.org', 'community_description': 'Clubs are awesome', 'community_imprint': 'You should provide your website imprint here.', 'community_privacy_policy': 'You should provide your website privacy policy here.', 'logged_interactions_count': 800, 'events_enabled': true,'events_count': 5,'event_votes_count': 2,'event_decisions_count': 15, 'event_dates_count': 6,'cinema_enabled': true,'movies_count': 3,'movies_tickets_count': 2, 'movies_workers_count': 3, 'broadcasts_enabled': true, 'broadcasts_count': 5,'broadcasts_sent_count': 2, 'users_count': 4,'user_email_addresses_count': 4, 'user_phone_numbers_count': 2, 'performance_badges_count': 0 } "
                )
        )

        Timber.i(retrofit.baseUrl().toString())
        val response = service.apiInfo()

        assertEquals(15, response.body()?.versionNumber)
    }
}