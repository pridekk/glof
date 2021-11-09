package com.pridekk.getlandonfoot.data.remote

import com.pridekk.getlandonfoot.data.remote.responses.Area
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GlofApi {

    @GET("/dev/areas")
    suspend fun getAreas(
        @Header("Authorization") authHeader: String
    ): Area
}