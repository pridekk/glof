package com.pridekk.getlandonfoot.ui.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Composable
fun Main(
    navController: NavController,
    onClickListener: (Context) -> Unit
){
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
                onClick = {onClickListener(context)}
            ){
                Text("위치추적 시작")
            }
        }

    }

}