package com.pridekk.getlandonfoot.data.remote

import com.pridekk.getlandonfoot.data.remote.responses.BizDay
import retrofit2.http.GET
import retrofit2.http.Query

interface SpecApi {

    @GET("/api/v2.1/stock/is-order-day")
    suspend fun getOrderDay(
        @Query("date") date: String
    ): BizDay
}