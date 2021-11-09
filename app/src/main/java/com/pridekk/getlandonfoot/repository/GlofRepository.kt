package com.pridekk.getlandonfoot.repository

import com.pridekk.getlandonfoot.data.remote.GlofApi
import com.pridekk.getlandonfoot.data.remote.responses.Area
import com.pridekk.getlandonfoot.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GlofRepository @Inject constructor(
    private val api: GlofApi
) {
    suspend fun getArea(authToken:String): Resource<Area> {
        val response = try {
            api.getAreas(authHeader = authToken)
        } catch(e: Exception){
            return Resource.Error(e.toString())
        }
        return Resource.Success(response)
    }
}