package com.pridekk.getlandonfoot.ui.components

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import java.lang.IllegalStateException
import java.lang.reflect.Modifier

@Composable
fun MyMap(
    navController: NavController,
    modifier: Modifier = Modifier(),
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
        }
    )

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