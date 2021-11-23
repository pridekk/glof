package com.pridekk.getlandonfoot

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.pridekk.getlandonfoot.repository.GlofRepository
import com.pridekk.getlandonfoot.repository.SpecRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    var loggedIn = MutableLiveData<Boolean>(false)

    var token by mutableStateOf("")
            private set

    var error by mutableStateOf("")

    var lastLocation = MutableLiveData<Location?>(null)

    init {
        val user = mAuth.currentUser
        if(user != null){
            loggedIn.value = true
            viewModelScope.launch {

                val task = user.getIdToken(false)
                if (task.isSuccessful) {
                    token = task.result.token.toString()

                    val result = glofRepository.getArea(token)
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
           loggedIn.value = true

           user.getIdToken(true).let { task ->
               if (task.isSuccessful) {
                   idToken = task.result.token
                   token = task.result.token.toString()
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

            val task = mAuth.signInWithEmailAndPassword(email,password)
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("loginUserWithEmail:success")
                    loggedIn.value = true
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.d("loginUserWithEmail:failure", it.exception)
                    it?.exception?.message?.let { errorMessage ->
                        error = errorMessage
                    }
                }
            }
        }
    }

    fun logout() = viewModelScope.launch {
        mAuth.signOut()
        loggedIn.value = false

    }

}