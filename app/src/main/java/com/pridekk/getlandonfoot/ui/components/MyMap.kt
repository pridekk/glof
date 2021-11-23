package com.pridekk.getlandonfoot.ui.components

import android.location.Location
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import timber.log.Timber


@ExperimentalPermissionsApi
@Composable
fun MyMap(
    lastLocation: Location?,
    onReady: (GoogleMap) -> Unit
){

    Timber.d("Recompose MyMap")
    val context = LocalContext.current

    val mapView = remember {
        MapView(context)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    lifecycle.addObserver(rememberMapLifecycle(map = mapView))

    val coroutineScope = rememberCoroutineScope()
    val DEFAULT_ZOOM = 15
    AndroidView(
        factory = {
            mapView.apply {
                mapView.getMapAsync { googleMap ->

                    Timber.d("Init Map: $lastLocation")
                    coroutineScope.launch {
                        Timber.d("Set Marker: $lastLocation")
                        lastLocation?.let {
                            val myLocation = LatLng(it.longitude, it.longitude)
                            Timber.d(myLocation.toString())
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(myLocation)
                                    .title("Your Position")

                            )
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        it.latitude,
                                        it.longitude), DEFAULT_ZOOM.toFloat()))
                            googleMap.uiSettings.isMyLocationButtonEnabled = true
                        }

                    }
                }
            }
        },
        update = {
            mapView.getMapAsync { googleMap ->

                Timber.d("ReDraw Map: $lastLocation")
                coroutineScope.launch {
                    Timber.d("Set Marker: $lastLocation")
                    lastLocation?.let {
                        val myLocation = LatLng(it.longitude, it.longitude)
                        Timber.d(myLocation.toString())
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(myLocation)
                                .title("Your Position")

                        )
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    it.latitude,
                                    it.longitude), DEFAULT_ZOOM.toFloat()))
                        googleMap.uiSettings.isMyLocationButtonEnabled = true
                    }

                }
            }

        },
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)
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


