package com.bke.datepoll.data.requests

import com.bke.datepoll.data.model.UserModel

data class CurrentUserResponseModel(
    val msg: String,
    val user : UserModel
)
