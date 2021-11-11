package com.pridekk.getlandonfoot.data.remote.requests

import com.google.gson.annotations.SerializedName

data class MyLocation(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("speed") val speed: Float
)
