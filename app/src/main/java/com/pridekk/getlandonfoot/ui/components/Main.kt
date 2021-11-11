package com.pridekk.getlandonfoot.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pridekk.getlandonfoot.services.TrackingService
import com.pridekk.getlandonfoot.ui.viewmodels.ProfileViewModel
import com.pridekk.getlandonfoot.utils.Constants.ACTION_PAUSE_SERVICE
import com.pridekk.getlandonfoot.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.pridekk.getlandonfoot.utils.Constants.ACTION_STOP_SERVICE
import timber.log.Timber

@ExperimentalPermissionsApi
@Composable
fun Main(
    navController: NavController,

    onClickListener: (Context) -> Unit,
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
    var isTracking = false
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

        MyMap(
        ){ googleMap ->
            val sydney = LatLng(-33.852, 151.211)
            googleMap.addMarker(
                MarkerOptions()
                    .position(sydney)
                    .title("Marker in Sydney")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                        if (isTracking) {
                            Intent(context, TrackingService::class.java).also {
                                it.action = ACTION_STOP_SERVICE
                                it.putExtra("TOKEN", profileViewModel.getToken() )
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    context.startForegroundService(it)
                                } else {
                                    context.startService(it)
                                }
                            }

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
                            Timber.d("Started service")
                        }
                    } else {
                        Toast.makeText(context, "위치 권한이 없습니다", Toast.LENGTH_LONG).show()
                    }
                }
            ){
                Text("위치추적 시작")
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