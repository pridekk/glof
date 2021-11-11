package com.pridekk.getlandonfoot.repository

import com.pridekk.getlandonfoot.data.remote.GlofApi
import com.pridekk.getlandonfoot.data.remote.requests.MyLocation
import com.pridekk.getlandonfoot.data.remote.responses.Area
import com.pridekk.getlandonfoot.data.remote.responses.MyLocationResponse
import com.pridekk.getlandonfoot.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class GlofRepository @Inject constructor(
    private val api: GlofApi
) {
    suspend fun getArea(authToken:String): Resource<Area> {
        val response = try {
            Timber.d("GetAreas")
            api.getAreas(authHeader = authToken)
        } catch(e: Exception){
            return Resource.Error(e.toString())
        }
        return Resource.Success(response)
    }

    suspend fun postLocations(authToken: String, locations: List<MyLocation>): Resource<MyLocationResponse> {
        val response = try {
            Timber.d("PostLocations: $authToken")
            api.postLocations(authHeader = authToken, locations = locations)
        } catch(e: Exception){
            Timber.d(e.toString())
            return Resource.Error(e.toString())
        }
        return Resource.Success(response)
    }

}