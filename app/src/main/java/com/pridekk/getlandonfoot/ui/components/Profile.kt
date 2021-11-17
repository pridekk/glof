package com.pridekk.getlandonfoot.ui.components

import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.pridekk.getlandonfoot.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.pridekk.getlandonfoot.R
import kotlin.math.log

@ExperimentalCoroutinesApi
@Composable
fun Profile(
    firebaseToken: String,
    isTracking: Boolean?,
    toggleTracking: () -> Unit,
    logout: () -> Unit,
){


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
        Card(
            modifier = Modifier.fillMaxHeight(0.5f)
        ){

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
                }

            }

        }

    }

}

@Preview
@Composable
fun ProfilePreview(){
    Profile(
        firebaseToken = "test",
        isTracking = false,
        toggleTracking = {}
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
