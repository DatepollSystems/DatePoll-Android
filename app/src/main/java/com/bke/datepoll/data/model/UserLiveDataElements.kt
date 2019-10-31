package com.bke.datepoll.data.model

import androidx.lifecycle.LiveData
import com.bke.datepoll.db.model.UserDbModel

data class UserLiveDataElements(
    val user: LiveData<UserDbModel>,
    val phoneNumbers: LiveData<List<PhoneNumberDbModel>>
)