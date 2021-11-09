package com.pridekk.getlandonfoot.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GlofApi {

    @GET("/dev/areas")
    suspend fun getAreas(
        @Header("Authorization") authHeader: String
    ): String
}