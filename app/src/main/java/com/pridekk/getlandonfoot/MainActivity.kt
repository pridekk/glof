package com.pridekk.getlandonfoot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import com.pridekk.getlandonfoot.ui.components.Login
import com.pridekk.getlandonfoot.ui.components.Navigation
import com.pridekk.getlandonfoot.ui.theme.GetLandOnFootTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity(){

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.loginUiState.collect {
                when(it) {
                    is MainViewModel.LoginUiState.Success -> {
                        setContent {
                            GetLandOnFootTheme {
                                // A surface container using the 'background' color from the theme
                                Surface(color = MaterialTheme.colors.background) {
                                    Navigation(viewModel::logout)
                                }
                            }
                        }
                    }
                    is MainViewModel.LoginUiState.Empty -> {
                        setContent {
                            GetLandOnFootTheme {
                                Surface(color = MaterialTheme.colors.background) {
                                    Login(viewModel::login)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}







