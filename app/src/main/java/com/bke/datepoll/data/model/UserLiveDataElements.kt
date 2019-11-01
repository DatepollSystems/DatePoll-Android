package com.bke.datepoll.data.model

import androidx.lifecycle.LiveData
import com.bke.datepoll.db.model.*

data class UserLiveDataElements(
    val user: LiveData<UserDbModel>,
    val phoneNumbers: LiveData<List<PhoneNumberDbModel>>,
    val performanceBadges: LiveData<List<PerformanceBadgesDbModel>>,
    val emailAddress: LiveData<List<EmailAddressDbModel>>,
    val permissions: LiveData<List<PermissionDbModel>>
)