package com.pridekk.getlandonfoot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navController: NavController){
    Column() {
        Text(text = "Profile")
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = { FirebaseAuth.getInstance().signOut() }) {
            Text(text = "로그아웃")

        }
    }

}