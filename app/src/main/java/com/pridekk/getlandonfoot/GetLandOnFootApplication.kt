package com.pridekk.getlandonfoot

import android.app.Application
import timber.log.Timber

class GetLandOnFootApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}