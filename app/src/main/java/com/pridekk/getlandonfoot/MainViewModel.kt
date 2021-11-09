package com.pridekk.getlandonfoot


import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.repository.GlofRepository
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
    private val repository: SpecRepository,
    private val glofRepository: GlofRepository
): ViewModel(), LifecycleObserver {

    private var mAuth = FirebaseAuth.getInstance()
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginUiState: StateFlow<LoginUiState> get() = _loginUiState

    private val _token = MutableStateFlow("")
    val token: StateFlow<String> get() = _token

    init {
        val user = mAuth.currentUser
        if(user != null){
            _loginUiState.value = LoginUiState.Success
            viewModelScope.launch {

                val task = user.getIdToken(false)
                if (task.isSuccessful) {
                    _token.value = task.result.token.toString()

                    val result = glofRepository.getArea(token.value)
                    result.data

                } else {
                    // Handle error -> task.getException();
                    val test: String = ""

                }
            }
        }

    }

    fun login(email:String, password:String) = viewModelScope.launch {
        val user = mAuth.currentUser
        var idToken: String? = null
        if(user != null){
           _loginUiState.value = LoginUiState.Success

           val task = user.getIdToken(false)
            if (task.isSuccessful) {
                idToken = task.result.token
                _token.value = task.result.token.toString()
                Timber.d("idToken ${idToken}")
            } else {
                // Handle error -> task.getException();
            }

        } else {
            _loginUiState.value = LoginUiState.Loading

            val task = mAuth.signInWithEmailAndPassword(email, password)
            if (task.isSuccessful) {
               // Sign in success, update UI with the signed-in user's information
               Timber.d("createUserWithEmail:success")
               val user = mAuth.currentUser
               if(user != null){
                   val task = user.getIdToken(true)
                   idToken = task.result.token
                   Timber.d("idToken ${idToken}")
                   _loginUiState.value = LoginUiState.Success
               }
            } else {
               // If sign in fails, display a message to the user.
               Timber.d("createUserWithEmail:failure", task.exception)
               _loginUiState.value = LoginUiState.Error(task.exception.toString())
            }


        }
        val response = repository.getOrderDay("2021-10-12")
        val res2 = idToken?.let { glofRepository.getArea(idToken) }
        res2?.data
//        response.data
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