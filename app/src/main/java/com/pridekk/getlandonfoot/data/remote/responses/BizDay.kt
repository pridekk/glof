package com.pridekk.getlandonfoot.data.remote.responses

import com.google.gson.annotations.SerializedName

data class BizDay(
    @SerializedName("date") val date:String,
    @SerializedName("orderDay") val orderDay: Boolean
)
