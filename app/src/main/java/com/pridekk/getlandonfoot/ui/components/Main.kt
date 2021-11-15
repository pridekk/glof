package com.pridekk.getlandonfoot.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pridekk.getlandonfoot.services.TrackingService
import com.pridekk.getlandonfoot.ui.viewmodels.ProfileViewModel
import com.pridekk.getlandonfoot.utils.Constants.ACTION_PAUSE_SERVICE
import com.pridekk.getlandonfoot.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.pridekk.getlandonfoot.utils.Constants.ACTION_STOP_SERVICE
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("StateFlowValueCalledInComposition", "MissingPermission")
@ExperimentalPermissionsApi
@Composable
fun Main(
    navController: NavController,
    onClickListener: (Context) -> Unit,
    fusedLocationClient: FusedLocationProviderClient,
    profileViewModel: ProfileViewModel = hiltViewModel()
){


    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if(event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
    var isTracking = TrackingService.isTracking.observeAsState()

    var curTimeInMillis = 0L
    var pathPoints = mutableListOf<MutableList<LatLng>>()
    val context = LocalContext.current
    val multiplePermissionsState = rememberMultiplePermissionsState(
        getRequiredPermissions()
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val coroutineScope = rememberCoroutineScope()
        val defaultLocation = LatLng(-33.8523341, 151.2106085)
        val DEFAULT_ZOOM = 15
        MyMap(
        ){ googleMap ->

            coroutineScope.launch {
                val task = fusedLocationClient.lastLocation
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    val lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        val myLocation = LatLng(lastKnownLocation.longitude, lastKnownLocation.longitude)
                        Timber.d(myLocation.toString())
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(myLocation)
                                .title("Your Position")

                        )
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                lastKnownLocation.latitude,
                                lastKnownLocation.longitude), DEFAULT_ZOOM.toFloat()))
                    }
                } else {
                    Timber.d("Current location is null. Using defaults.")
                    Timber.d("Exception: %s", task.exception)
                    googleMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                    googleMap.uiSettings.isMyLocationButtonEnabled = true
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text="메인 화면",
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    var hasAllPermission = true
                    permissionsState.permissions.forEach { perm ->
                        if(!perm.hasPermission){
                            hasAllPermission = false
                        }
                    }
                    if (hasAllPermission){
                        if (isTracking.value == true) {
                            Intent(context, TrackingService::class.java).also {
                                it.action = ACTION_STOP_SERVICE
                                it.putExtra("TOKEN", profileViewModel.getToken() )
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    context.startForegroundService(it)
                                } else {
                                    context.startService(it)
                                }
                            }
                            Timber.d("Tracking Stopped Service")

                        } else {
                            Intent(context, TrackingService::class.java).also {
                                it.action = ACTION_START_OR_RESUME_SERVICE
                                it.putExtra("TOKEN", profileViewModel.getToken() )

                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    context.startForegroundService(it)
                                } else {
                                    context.startService(it)
                                }
                            }
                            Timber.d("Tracking Started service")
                        }

                    } else {
                        Toast.makeText(context, "위치 권한이 없습니다", Toast.LENGTH_LONG).show()
                    }
                }
            ){
                if(isTracking.value == true){
                    Text("트래킹 중지")
                } else {
                    Text("트래킹 시작")
                }

            }
        }
    }


}

fun getRequiredPermissions(): List<String>{
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        return listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }
    return listOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
}