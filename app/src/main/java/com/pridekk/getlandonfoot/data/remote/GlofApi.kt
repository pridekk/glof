package com.pridekk.getlandonfoot.data.remote

import com.pridekk.getlandonfoot.data.remote.requests.MyLocation
import com.pridekk.getlandonfoot.data.remote.responses.Area
import com.pridekk.getlandonfoot.data.remote.responses.MyLocationResponse
import retrofit2.Response
import retrofit2.http.*

interface GlofApi {

    @GET("/dev/areas")
    suspend fun getAreas(
        @Header("Authorization") authHeader: String
    ): Area

    @Headers("Content-Type: application/json")
    @POST("/dev/locations")
    suspend fun postLocations(
        @Header("Authorization") authHeader: String,
        @Body locations: List<MyLocation>
    ) : MyLocationResponse
}