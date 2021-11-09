package com.pridekk.getlandonfoot.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pridekk.getlandonfoot.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun Login(
    login: (email: String, password: String) -> Unit,
    viewModel: MainViewModel?,
    ){
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("EMAIL")}
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text("PASSWORD")},
            placeholder = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(imageVector  = image, "")
                }
            }

            )

        Spacer(
            modifier = Modifier.padding(10.dp)
        )
        Button(onClick = {
            login(email, password)
        }) {
            Text(text = "로그인")
        }
        if (viewModel?.loginUiState?.value is MainViewModel.LoginUiState.Error ){
            val context = LocalContext.current
            Toast.makeText(
                context,
                viewModel.loginUiState.value.toString(),
                Toast.LENGTH_LONG
            ).show()
            viewModel.setLoginUiState(MainViewModel.LoginUiState.Empty)

        }
    }
}

@ExperimentalCoroutinesApi
@Preview
@Composable
fun LoginPreview(){
    Login({ email, password ->

    }, null )
}