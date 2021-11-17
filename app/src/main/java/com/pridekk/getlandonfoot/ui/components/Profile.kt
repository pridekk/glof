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
@ExperimentalCoroutinesApi
@Composable
fun Profile(
    firebaseToken: String,
    logout: () -> Unit,
){


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Card() {
            Row(modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(4.dp))
                .background(color = MaterialTheme.colors.background)
                .padding(16.dp)){
                Column(){
                    Image(
                        painter = painterResource(R.drawable.profile_pic),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(48.dp),
                        contentDescription = "Profile picture holder"
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
                ){
                    Text("Catalin Ghita", fontWeight = FontWeight.Bold)
                    Text(
                        text = "Active now",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
        Text(text = "Token")
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = firebaseToken)
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = {
            logout()
        }
        ) {
            Text(text = "로그아웃")

        }
    }

}

@Preview
@Composable
fun ProfilePreview(){
    Profile(firebaseToken = "test") {}
}
