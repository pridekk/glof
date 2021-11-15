package com.pridekk.getlandonfoot


import android.content.Intent
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.repository.GlofRepository
import com.pridekk.getlandonfoot.repository.SpecRepository
import com.pridekk.getlandonfoot.services.TrackingService
import com.pridekk.getlandonfoot.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val repository: SpecRepository,
    private val glofRepository: GlofRepository
): ViewModel(), LifecycleObserver {

    private var mAuth = FirebaseAuth.getInstance()
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginUiState: StateFlow<LoginUiState> get() = _loginUiState

    private val _token = MutableStateFlow("")
    val token: StateFlow<String> get() = _token

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String> get() = _error

    init {
        val user = mAuth.currentUser
        if(user != null){
            _loginUiState.value = LoginUiState.Success
            viewModelScope.launch {

                val task = user.getIdToken(false)
                if (task.isSuccessful) {
                    _token.value = task.result.token.toString()

                    val result = glofRepository.getArea(token.value)
                    Timber.d(result.data?.message)

                } else {
                    Timber.d("Logged in user getIdToken Failed")
                }
            }
        }

    }

    fun login(email:String, password:String) = viewModelScope.launch {
        val user = mAuth.currentUser
        var idToken: String? = null
        if(user != null){
           _loginUiState.value = LoginUiState.Success

           user.getIdToken(true).let { task ->
               if (task.isSuccessful) {
                   idToken = task.result.token
                   _token.value = task.result.token.toString()
                   Timber.d("1idToken ${idToken}")
                   idToken?.let {
                       val result = glofRepository.getArea(it)
                       Timber.d(result.message)
                   }

               } else {
                   Timber.d("New Log in user getIdToken Failed")
               }
           }

        } else {
            _loginUiState.value = LoginUiState.Loading
            val task = mAuth.signInWithEmailAndPassword(email,password)
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("loginUserWithEmail:success")
                    _loginUiState.value = LoginUiState.Success
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.d("loginUserWithEmail:failure", it.exception)
                    _loginUiState.value = LoginUiState.Error(it.exception.toString())

                }
            }
        }
    }

    fun logout() = viewModelScope.launch {
        mAuth.signOut()
        _loginUiState.value = LoginUiState.Empty

    }


    private fun handleError(exception: Throwable){
        _error.value = exception.message.toString()
    }

    fun setLoginUiState(loginState: MainViewModel.LoginUiState) {
        _loginUiState.value = loginState
    }

    sealed class LoginUiState {
        object Success: LoginUiState()
        data class Error(val message: String): LoginUiState()
        object Loading: LoginUiState()
        object Empty: LoginUiState()
    }
}