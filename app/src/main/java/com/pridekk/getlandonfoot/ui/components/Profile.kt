package com.pridekk.getlandonfoot.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pridekk.getlandonfoot.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.pridekk.getlandonfoot.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import kotlin.collections.mutableSetOf
import kotlin.math.log
import kotlin.time.Duration

@SuppressLint("MissingPermission")
@ExperimentalPermissionsApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalCoroutinesApi
@Composable
fun Profile(
    firebaseToken: String,
    isTracking: Boolean?,
    toggleTracking: () -> Unit,
    fusedLocationClient: FusedLocationProviderClient?,
    logout: () -> Unit,
){

    var trackingTime by remember {
        mutableStateOf(0L)
    }

    val localContext = LocalContext.current

    var trackingStarted: LocalDateTime? by rememberSaveable {
        mutableStateOf(null)
    }

    var lastKnownLocation: Location? by remember {
        mutableStateOf(null)
    }
    LaunchedEffect(key1 = isTracking, key2 = trackingTime, key3 = trackingStarted){
        if(isTracking == true){
            delay(1000L)
            var currentTime = LocalDateTime.now()
            if(trackingStarted == null){
                trackingStarted = LocalDateTime.now()
            }else{
                java.time.Duration.between(trackingStarted, currentTime).also {
                    trackingTime = it.seconds
                    val task = fusedLocationClient?.lastLocation
                    task?.isSuccessful.let {

                        if(it == true){
                            lastKnownLocation = task!!.result
                        }

                    }


                }
            }
        } else {
            trackingStarted = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 3.dp
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(color = MaterialTheme.colors.background)
                .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(R.drawable.img_default_profile),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(100.dp),
                        contentDescription = "Profile picture holder"
                    )
                    Text("Pridekk")

                }
                Column(
                    modifier = Modifier
                        .weight(3f)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
                ){
                    MyStat(statName = "보유영역", statValue = "11")
                    MyStat(statName = "랜드마크", statValue = "3" )
                    MyStat(statName = "보유도토리", statValue = "124")
                    MyStat(statName = "순위", statValue = "1")
                }
            }
        }
        Row(){
            MyStat(statName = "Token", statValue = firebaseToken.take(10))
        }
        Column(

            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(1f)
                .padding(10.dp)
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            val coroutineScope = rememberCoroutineScope()
            val defaultLocation = LatLng(-33.8523341, 151.2106085)
            val DEFAULT_ZOOM = 15

            if(isTracking == true){
                MyStat(statName = "추적시간", statValue = trackingTime.toString())
                MyMap(
                ){ googleMap ->

                    coroutineScope.launch {

                        lastKnownLocation?.let {
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
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = toggleTracking
            ) {
                if(isTracking == true){
                    Text(text = "Stop GLOF")

                } else {
                    Text(text = "Start GLOF")
                    trackingTime = 0L
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ProfilePreview(){
    Profile(
        firebaseToken = "test",
        isTracking = true,
        toggleTracking = {},
        null
    ) {}
}

@Composable
fun MyStat(
    statName: String,
    statValue: String
){
    Row(
        modifier = Modifier.padding(2.dp)
    ){
        Text(
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.body2,
            text=statName
        )
        Text(
            modifier = Modifier.weight(4f),
            text=statValue
        )
    }

}
