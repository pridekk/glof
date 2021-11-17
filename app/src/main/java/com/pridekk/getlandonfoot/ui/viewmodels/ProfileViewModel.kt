package com.pridekk.getlandonfoot.ui.viewmodels

import android.content.Intent
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.MainViewModel
import com.pridekk.getlandonfoot.repository.GlofRepository
import com.pridekk.getlandonfoot.services.TrackingService
import com.pridekk.getlandonfoot.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val glofRepository: GlofRepository
): ViewModel() {
    var token by mutableStateOf("")

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {

        mAuth.currentUser?.let {
            val task = it.getIdToken(false)
            if (task.isSuccessful) {
                token = task.result.token.toString()

            } else {
                Timber.d("User is not logged in")
            }
        }

    }

}