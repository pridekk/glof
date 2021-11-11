package com.pridekk.getlandonfoot.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.pridekk.getlandonfoot.services.TrackingService
import com.pridekk.getlandonfoot.utils.Constants.ACTION_PAUSE_SERVICE
import com.pridekk.getlandonfoot.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.pridekk.getlandonfoot.utils.Constants.ACTION_STOP_SERVICE
import timber.log.Timber

@Composable
fun Main(
    navController: NavController,
    onClickListener: (Context) -> Unit
){
    var isTracking = false
    var curTimeInMillis = 0L
    var pathPoints = mutableListOf<MutableList<LatLng>>()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(10.dp)
    ){
        Card() {
            Text(
                text="메인 화면",

            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (isTracking) {
                        Intent(context, TrackingService::class.java).also {
                            it.action = ACTION_STOP_SERVICE
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                context.startForegroundService(it)
                            } else {
                                context.startService(it)
                            }
                        }

                    } else {
                        Intent(context, TrackingService::class.java).also {
                            it.action = ACTION_START_OR_RESUME_SERVICE
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                context.startForegroundService(it)
                            } else {
                                context.startService(it)
                            }
                        }
                        Timber.d("Started service")
                    }

                }
            ){
                Text("위치추적 시작")
            }
        }

    }




}