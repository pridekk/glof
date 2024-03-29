package com.pridekk.getlandonfoot.ui.components

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pridekk.getlandonfoot.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.pridekk.getlandonfoot.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime

@SuppressLint("MissingPermission")
@ExperimentalPermissionsApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalCoroutinesApi
@Composable
fun Profile(
    firebaseToken: String,
    isTracking: Boolean?,
    toggleTracking: () -> Unit,
    lastLocations: MutableList<Location>?,
    logout: () -> Unit

){
    Timber.d("Recompose Profile: $lastLocations")

    var trackingTime by remember {
        mutableStateOf(0L)
    }

    var trackingStarted: LocalDateTime? by rememberSaveable {
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
            val DEFAULT_ZOOM = 15

            if(isTracking == true){
                MyStat(statName = "추적시간", statValue = trackingTime.toString())
                MyMap(lastLocations){}
            } else {
                if(lastLocations?.isNotEmpty() == true){
                    MyMap(lastLocations){}
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

@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ProfilePreview(){
    val location = Location("dummy" )
    location.longitude = 22.1
    location.altitude = 22.2

    Profile(
        firebaseToken = "test",
        isTracking = true,
        toggleTracking = {},
        mutableListOf(location)
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
