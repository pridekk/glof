package com.pridekk.getlandonfoot.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import com.pridekk.getlandonfoot.services.TrackingService
import timber.log.Timber

fun Context.foregroundStartService(command: String){
    Timber.d("Start foreground Service")
    val intent = Intent(this, TrackingService::class.java)
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        this.startForegroundService(intent)
    } else {
        this.startService(intent)
    }
}