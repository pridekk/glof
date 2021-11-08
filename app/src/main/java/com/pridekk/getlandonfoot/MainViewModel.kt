package com.pridekk.getlandonfoot


import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.repository.SpecRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val repository: SpecRepository
): ViewModel(), LifecycleObserver {

    private var mAuth = FirebaseAuth.getInstance()
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginUiState: StateFlow<LoginUiState> get() = _loginUiState

    init {
        val user = mAuth.currentUser
        if(user != null){
            _loginUiState.value = LoginUiState.Success
        }

    }

    fun login(email:String, password:String) = viewModelScope.launch {
        val user = mAuth.currentUser
        if(user != null){
           _loginUiState.value = LoginUiState.Success

           val task = user.getIdToken(true)
            if (task.isSuccessful) {
                val idToken: String? = task.result.token
                Timber.d("idToken ${idToken}")
            } else {
                // Handle error -> task.getException();
            }

        } else {
            _loginUiState.value = LoginUiState.Loading

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   // Sign in success, update UI with the signed-in user's information
                   Timber.d("createUserWithEmail:success")
                   val user = mAuth.currentUser
                   if(user != null){
                       _loginUiState.value = LoginUiState.Success
                   }
               } else {
                   // If sign in fails, display a message to the user.
                   Timber.d("createUserWithEmail:failure", task.exception)
                   _loginUiState.value = LoginUiState.Error(task.exception.toString())
               }
           }

        }
        val response = repository.getOrderDay("2021-10-12")

        response.data
    }

    fun logout() = viewModelScope.launch {
        mAuth.signOut()
        _loginUiState.value = LoginUiState.Empty
    }

    sealed class LoginUiState {
        object Success: LoginUiState()
        data class Error(val message: String): LoginUiState()
        object Loading: LoginUiState()
        object Empty: LoginUiState()
    }
}