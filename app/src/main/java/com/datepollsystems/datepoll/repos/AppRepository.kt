package com.datepollsystems.datepoll.repos

import androidx.lifecycle.MutableLiveData
import com.datepollsystems.datepoll.core.BaseRepository
import com.datepollsystems.datepoll.core.ENetworkState
import com.datepollsystems.datepoll.db.DatepollDatabase
import com.datepollsystems.datepoll.network.InstanceApi
import org.koin.core.component.inject
import java.util.*

class AppRepository : BaseRepository() {


    private val db: DatepollDatabase by inject()
    private val apiDao by lazy { db.apiDao() }

    val apiInfo = apiDao.getApiFlow()

    suspend fun loadApiInfo(state: MutableLiveData<ENetworkState?> = MutableLiveData()){
        val api: InstanceApi = getKoin().get()
        val apiModel = apiDao.getApi()
        val force = apiDao.countOfApi() == 0
        if (force || Date().time - apiModel.insertedAt  > 60000) {
            val res = apiCall(
                call = { api.apiInfo() },
                state = state
            )

            res?.let {
                val result = it
                result.insertedAt = Date().time
                apiDao.insertApi(result)
            }
        }
    }
}