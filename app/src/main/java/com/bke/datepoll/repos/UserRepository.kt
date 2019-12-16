package com.bke.datepoll.repos

import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.db.DatepollDatabase
import com.bke.datepoll.db.model.UserDbModel
import java.util.*

class UserRepository(private val api: DatepollApi, private val db: DatepollDatabase) : BaseRepository("UserRepository") {

    fun loadUser(): UserDbModel{

        val user = db.userDao().getAllUsers().value!![0]

        if((Date().time - user.savedAt!!) > 3600000){
            //load it from server
            return user
        } else {
            return user
        }


    }
}