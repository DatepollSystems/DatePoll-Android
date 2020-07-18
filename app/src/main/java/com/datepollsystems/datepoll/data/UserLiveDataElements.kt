package com.datepollsystems.datepoll.data

import androidx.lifecycle.LiveData

data class UserLiveDataElements(
    val user: LiveData<UserDbModel>,
    val phoneNumbers: LiveData<List<PhoneNumberDbModel>>,
    val performanceBadges: LiveData<List<PerformanceBadgesDbModel>>,
    val emailAddress: LiveData<List<EmailAddressDbModel>>,
    val permissions: LiveData<List<PermissionDbModel>>
)