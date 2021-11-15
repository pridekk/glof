package com.pridekk.getlandonfoot.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.repository.GlofRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

class TrackingViewModel @Inject constructor(
    private val glofRepository: GlofRepository
): ViewModel() {
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> get() = _token
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var isTracking: Boolean = false

    init {

        mAuth.currentUser?.let {
            val task = it.getIdToken(false)
            if (task.isSuccessful) {
                _token.value = task.result.token.toString()

            } else {
                Timber.d("User is not logged in")
            }
        }

    }

    fun getToken() = token.value

}