package com.pridekk.getlandonfoot.ui.components

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import timber.log.Timber
import java.lang.IllegalStateException


@ExperimentalPermissionsApi
@Composable
fun MyMap(
    navController: NavController,
    fusedLocationClient: FusedLocationProviderClient,
    modifier: Modifier = Modifier,
    onReady: (GoogleMap) -> Unit,

){
    val context = LocalContext.current

//    val mapView = remember {
//        MapView(context)
//    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle


    val multiplePermissionsState = rememberMultiplePermissionsState(
        getRequiredPermissions()
    )

//    lifecycle.addObserver(rememberMapLifecycle(map = mapView))

    Column(){
//        AndroidView(
//            factory = {
//                mapView.apply {
//                    mapView.getMapAsync { googleMap ->
//                        onReady(googleMap)
//                    }
//                }
//            }
//        )
        PermissionsRequest(
            multiplePermissionsState,
            fusedLocationClient,
            navigateToSettingsScreen = {
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                )
            }
        )

    }


}

@Composable
fun rememberMapLifecycle(map:MapView): LifecycleEventObserver {
    return remember {
        LifecycleEventObserver { source, event ->
            when(event){
                Lifecycle.Event.ON_CREATE ->  map.onCreate(Bundle())
                Lifecycle.Event.ON_START ->  map.onStart()
                Lifecycle.Event.ON_RESUME ->  map.onResume()
                Lifecycle.Event.ON_PAUSE ->  map.onPause()
                Lifecycle.Event.ON_DESTROY ->  map.onDestroy()
                Lifecycle.Event.ON_ANY ->  throw IllegalStateException()

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
