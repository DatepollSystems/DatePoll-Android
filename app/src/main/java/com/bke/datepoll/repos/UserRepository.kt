package com.bke.datepoll.repos

import androidx.lifecycle.LiveData
import com.bke.datepoll.connection.DatepollApi
import com.bke.datepoll.data.model.UserLiveDataElements
import com.bke.datepoll.data.model.UserModel
import com.bke.datepoll.db.DatepollDatabase
import com.bke.datepoll.db.dao.*
import com.bke.datepoll.db.model.EmailAddressDbModel
import com.bke.datepoll.db.model.PerformanceBadgesDbModel
import com.bke.datepoll.db.model.PermissionDbModel
import com.bke.datepoll.db.model.UserDbModel
import java.util.*

class UserRepository(private val api: DatepollApi, private val db: DatepollDatabase) : BaseRepository("UserRepository") {

    private val userDao: UserDao = db.userDao()
    private val phoneNumberDao: PhoneNumberDao = db.phoneDao()
    private val emailDao: EmailDao = db.emailDao()
    private val performanceBadgesDao: PerformanceBadgesDao = db.performanceBadgesDao()
    private val permissionsDao: PermissionsDao = db.permissionDao()

    suspend fun loadUser(force: Boolean = false): LiveData<UserDbModel>{
        val userLiveData = db.userDao().getUser()
        val user: UserDbModel = userLiveData.value!!


        return if(db.userDao().getCount() != 1L){
            val reloadedUser = loadUserFromServer()
            val userLiveDataElements: UserLiveDataElements = storeUser(reloadedUser)
            userLiveDataElements.user
        } else if((Date().time - user.savedAt) > 3600000 || force){
            //user is older then 1 hour -> reload user from server
            val reloadedUser = loadUserFromServer()
            //update user TODO update also child tables!!!
            userDao.addUser(reloadedUser.getUserDbModelPart())

            userDao.getUser()
        } else {
            userLiveData
        }
    }

    private suspend fun loadUserFromServer(): UserModel{
        return safeApiCall(
            api,
            call = { api.currentUser(prefs.JWT!!) },
            errorMessage = "Could not get new user")!!.user
    }

    private fun storeUser(user: UserModel): UserLiveDataElements {
        val userId = userDao.addUser(user.getUserDbModelPart())

        val performanceBadgesToStore = ArrayList<PerformanceBadgesDbModel>()
        val emailsToStore = ArrayList<EmailAddressDbModel>()
        val permissionsToStore = ArrayList<PermissionDbModel>()

        if(user.phone_numbers.isNullOrEmpty())
            phoneNumberDao.saveSetOfPhoneNumbers(user.phone_numbers)

        if(user.email_addresses.isNotEmpty()){

            user.email_addresses.forEach {
                emailsToStore.add(EmailAddressDbModel(0, it, user.id))
            }

            emailDao.addEmails(emailsToStore)
        }

        if(user.performance_badges.isNotEmpty()){

            user.performance_badges.forEach {
                performanceBadgesToStore.add(it.getPerformanceBadgesDbModel(user.id))
            }

            performanceBadgesDao.addPerformanceBadges(performanceBadgesToStore)
        }

        if(user.permissions.isNotEmpty()){
            user.permissions.forEach {
                permissionsToStore.add(PermissionDbModel(0, it, user.id))
            }

            permissionsDao.addPermissions(permissionsToStore)
        }


        return UserLiveDataElements(
            user = userDao.getUserById(user.id),
            phoneNumbers = phoneNumberDao.getPhoneNumbersForUser(user.id),
            permissions = permissionsDao.getAllPermissionsByUserId(user.id),
            performanceBadges = performanceBadgesDao.getPerformanceBadgesByUserId(user.id),
            emailAddress = emailDao.getEmailsOfUser(user.id)
        )
    }
}