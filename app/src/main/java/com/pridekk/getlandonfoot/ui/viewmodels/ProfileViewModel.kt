package com.pridekk.getlandonfoot.ui.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.MainViewModel
import com.pridekk.getlandonfoot.repository.GlofRepository
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
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> get() = _token
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

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