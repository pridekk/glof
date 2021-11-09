package com.pridekk.getlandonfoot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.MainViewModel
import com.pridekk.getlandonfoot.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun Profile(
    navController: NavController,
    logout: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
){
    Column() {
        Text(text = "Profile")
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = profileViewModel.getToken())
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = {
            logout()
        }
        ) {
            Text(text = "로그아웃")

        }
    }

}