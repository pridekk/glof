package com.pridekk.getlandonfoot

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pridekk.getlandonfoot.MainViewModel.LoginUiState.Error
import com.pridekk.getlandonfoot.services.TrackingService
import com.pridekk.getlandonfoot.ui.components.Login
import com.pridekk.getlandonfoot.ui.components.Navigation
import com.pridekk.getlandonfoot.ui.theme.GetLandOnFootTheme
import com.pridekk.getlandonfoot.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity(){

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isTacking = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        lifecycleScope.launchWhenCreated {
            viewModel.loginUiState.collect {
                when(it) {
                    is MainViewModel.LoginUiState.Success -> {
                        setContent {
                            GetLandOnFootTheme {
                                // A surface container using the 'background' color from the theme
                                Surface(color = MaterialTheme.colors.background) {
                                    Navigation(::logout, fusedLocationClient)
                                }
                            }
                        }
                    }
                    is MainViewModel.LoginUiState.Empty, is Error -> {
                        setContent {
                            GetLandOnFootTheme {
                                Surface(color = MaterialTheme.colors.background) {
                                    Login(viewModel::login,viewModel)
                                }
                            }
                        }
                    }

                }
            }

        }

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


}







