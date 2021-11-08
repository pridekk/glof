package com.pridekk.getlandonfoot.repository

import com.pridekk.getlandonfoot.data.remote.SpecApi
import com.pridekk.getlandonfoot.data.remote.responses.BizDay
import com.pridekk.getlandonfoot.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SpecRepository @Inject constructor(
    private val api: SpecApi
) {
    suspend fun getOrderDay(date: String): Resource<BizDay> {
        val response = try {
            api.getOrderDay(date)
        } catch(e: Exception){
            return Resource.Error("error occurred")
        }
        return Resource.Success(response)
    }
}