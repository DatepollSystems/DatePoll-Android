package com.datepollsystems.datepoll.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(tableName = "api")
data class ApiModel(

    @Transient
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Int = 1,

    @Transient
    @ColumnInfo(name = "inserted_at")
    var insertedAt: Long = 0,

    @field:Json(name = "version")
    @ColumnInfo(name = "version")
    var version: String,

    @field:Json(name = "version_number")
    @ColumnInfo(name = "version_number")
    var versionNumber: Int,

    @field:Json(name = "application_url")
    @ColumnInfo(name = "application_url")
    var applicationUrl: String,

    @field:Json(name = "community_name")
    @ColumnInfo(name = "community_name")
    var communityName: String,

    @field:Json(name = "community_url")
    @ColumnInfo(name = "community_url")
    var communityUrl: String,

    @field:Json(name = "community_description")
    @ColumnInfo(name = "community_description")
    var communityDescription: String,

    @field:Json(name = "community_imprint")
    @ColumnInfo(name = "community_imprint")
    var communityImprint: String,

    @field:Json(name = "community_privacy_policy")
    @ColumnInfo(name = "community_privacy_policy")
    var communityPrivacyPolicy: String,

    @field:Json(name = "events_enabled")
    @ColumnInfo(name = "events_enabled")
    var eventsEnabled: Boolean,

    @field:Json(name = "events_count")
    @ColumnInfo(name = "events_count")
    var eventsCount: Int,

    @field:Json(name = "event_votes_count")
    @ColumnInfo(name = "event_votes_count")
    var eventVotesCount: Int,

    @field:Json(name = "event_decisions_count")
    @ColumnInfo(name = "event_decisions_count")
    var eventDecisionsCount: Int,

    @field:Json(name = "event_dates_count")
    @ColumnInfo(name = "event_dates_count")
    var eventDatesCount: Int,

    @field:Json(name = "cinema_enabled")
    @ColumnInfo(name = "cinema_enabled")
    var cinemaEnabled: Boolean,

    @field:Json(name = "movies_count")
    @ColumnInfo(name = "movies_count")
    var moviesCount: Int,

    @field:Json(name = "movies_tickets_count")
    @ColumnInfo(name = "movies_tickets_count")
    var moviesTicketsCount: Int,

    @field:Json(name = "movies_workers_count")
    @ColumnInfo(name = "movies_workers_count")
    var moviesWorkersCount: Int,

    @field:Json(name = "broadcasts_enabled")
    @ColumnInfo(name = "broadcasts_enabled")
    var broadcastsEnabled: Boolean,

    @field:Json(name = "broadcasts_count")
    @ColumnInfo(name = "broadcasts_count")
    var broadcastsCount: Int,

    @field:Json(name = "broadcasts_sent_count")
    @ColumnInfo(name = "broadcasts_sent_count")
    var broadcastsSentCount: Int,

    @field:Json(name = "users_count")
    @ColumnInfo(name = "users_count")
    var usersCount: Int,

    @field:Json(name = "user_email_addresses_count")
    @ColumnInfo(name = "user_email_addresses_count")
    var userEmailAddressesCount: Int,

    @field:Json(name = "user_phone_numbers_count")
    @ColumnInfo(name = "user_phone_numbers_count")
    var userPhoneNumbersCount: Int,

    @field:Json(name = "performance_badges_count")
    @ColumnInfo(name = "performance_badges_count")
    var performanceBadgesCount: Int
) {
    constructor(
        version: String,
        versionNumber: Int,
        applicationUrl: String,
        communityName: String,
        communityUrl: String,
        communityDescription: String,
        communityImprint: String,
        communityPrivacyPolicy: String,
        eventsEnabled: Boolean,
        eventsCount: Int,
        eventVotesCount: Int,
        eventDecisionsCount: Int,
        eventDatesCount: Int,
        cinemaEnabled: Boolean,
        moviesCount: Int,
        moviesTicketsCount: Int,
        moviesWorkersCount: Int,
        broadcastsEnabled: Boolean,
        broadcastsCount: Int,
        broadcastsSentCount: Int,
        usersCount: Int,
        userEmailAddressesCount: Int,
        userPhoneNumbersCount: Int,
        performanceBadgesCount: Int
    ) : this(1, Date().time, version,
        versionNumber,
        applicationUrl,
        communityName,
        communityUrl,
        communityDescription ,
        communityImprint ,
        communityPrivacyPolicy,
        eventsEnabled,
        eventsCount ,
        eventVotesCount ,
        eventDecisionsCount ,
        eventDatesCount ,
        cinemaEnabled ,
        moviesCount ,
        moviesTicketsCount ,
        moviesWorkersCount,
        broadcastsEnabled ,
        broadcastsCount ,
        broadcastsSentCount,
        usersCount,
        userEmailAddressesCount,
        userPhoneNumbersCount,
        performanceBadgesCount)
}

