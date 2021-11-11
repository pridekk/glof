package com.pridekk.getlandonfoot.ui.components

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
    onReady: (GoogleMap) -> Unit
){
    val context = LocalContext.current

    val mapView = remember {
        MapView(context)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    lifecycle.addObserver(rememberMapLifecycle(map = mapView))



    AndroidView(
        factory = {
            mapView.apply {
                mapView.getMapAsync { googleMap ->
                    onReady(googleMap)
                }
            }
        },
        Modifier.fillMaxWidth().fillMaxHeight(0.8f)
    )



}

@Composable
fun rememberMapLifecycle(map:MapView): LifecycleEventObserver {
    return remember {
        LifecycleEventObserver { source, event ->
            when(event){
                Lifecycle.Event.ON_CREATE ->  map.onCreate(Bundle())
                Lifecycle.Event.ON_START ->  {
                    Timber.d("Map onStart")
                    map.onStart()
                }
                Lifecycle.Event.ON_RESUME ->  map.onResume()
                Lifecycle.Event.ON_PAUSE ->  {
                    Timber.d("Map onPause")
                    map.onPause()
                }
                Lifecycle.Event.ON_DESTROY ->  {
                    Timber.d("Map onDestroy")
                    map.onDestroy()
                }
                Lifecycle.Event.ON_ANY ->  throw IllegalStateException()

            }

        }
    }
}


