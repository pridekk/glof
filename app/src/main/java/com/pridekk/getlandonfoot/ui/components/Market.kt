package com.pridekk.getlandonfoot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Market(){
    Column(

        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text( "Market")
    }
}