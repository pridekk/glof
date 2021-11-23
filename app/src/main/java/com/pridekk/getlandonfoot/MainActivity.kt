package com.pridekk.getlandonfoot

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pridekk.getlandonfoot.services.TrackingService
import com.pridekk.getlandonfoot.ui.components.Login
import com.pridekk.getlandonfoot.ui.components.Navigation
import com.pridekk.getlandonfoot.ui.theme.GetLandOnFootTheme
import com.pridekk.getlandonfoot.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity(){

    private val viewModel by viewModels<MainViewModel>()

    private var isTracking = MutableLiveData<Boolean>(false)

    private var loggedIn = MutableLiveData<Boolean>(false)

    private var lastLocation = MutableLiveData<Location?>(null)

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationObserver = Observer<Location?> {
            Timber.d("Location Changed to $it")
            viewModel.lastLocation.value = it
        }
        val trackingObserver = Observer<Boolean> {
            Timber.d("Tracking value Changed to $it")
            isTracking.value = it
        }

        val loginObserver = Observer<Boolean> {
            loggedIn.value = it
            if (it) {
                setContent {
                    GetLandOnFootTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colors.background) {
                            Navigation(
                                isTracking,
                                lastLocation,
                                ::toggleTrackingService,
                                ::logout
                            )
                        }
                    }
                }
            } else {
                setContent {
                    GetLandOnFootTheme {
                        Surface(color = MaterialTheme.colors.background) {
                            Login(viewModel::login,viewModel)
                        }
                    }
                }
            }
        }
        TrackingService.isTracking.observe(this, trackingObserver)
        TrackingService.lastLocation.observe(this,locationObserver)
        viewModel.loggedIn.observe(this,loginObserver)
    }

    private fun logout(){
        viewModel.logout()
        if(TrackingService.isTracking.value == true){
            Timber.d("Stop Tracking service")
            Intent(this, TrackingService::class.java).also {
                it.action = Constants.ACTION_STOP_SERVICE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(it)
                } else {
                    startService(it)
                }
            }
        }

    }

    private val permissionsState = mutableStateListOf(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    private fun toggleTrackingService() {
        var hasAllPermission = true

        if (hasAllPermission){
            if (TrackingService.isTracking.value == true) {
                Intent(this, TrackingService::class.java).also {
                    it.action = Constants.ACTION_STOP_SERVICE
                    it.putExtra("TOKEN", viewModel.token )
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        startForegroundService(it)
                    } else {
                        startService(it)
                    }
                }
                Timber.d("Tracking Stopped Service")

            } else {
                Intent(this, TrackingService::class.java).also {
                    it.action = Constants.ACTION_START_OR_RESUME_SERVICE
                    it.putExtra("TOKEN", viewModel.token )

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        startForegroundService(it)
                    } else {
                        startService(it)
                    }
                }
                Timber.d("Tracking Started service")
            }

        } else {
            Toast.makeText(this, "위치 권한이 없습니다", Toast.LENGTH_LONG).show()
        }
    }



}







