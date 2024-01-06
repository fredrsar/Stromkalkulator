package com.example.strmpriskalkulator.ui.screens.testscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strmpriskalkulator.R

@Composable
fun NoInternetConnectionScreen() {
    val errorIcon = painterResource(R.drawable.baseline_warning_24)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Icon(painter = errorIcon, contentDescription = "[errorIcon]", tint = colors.error)
        Text(
            text = "Ingen internett forbindelse!",
            style = typography.body2,
            fontSize = 25.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Hvordan løse problemet:",
            style = typography.body2
        )
        Text(
            text = "1. Avslutt appen",
            style = typography.h1
        )
        Text(
            text = "2. Sjekk internettforbindelsen",
            style = typography.h1
        )
        Text(
            text = "3. Start appen på nytt",
            style = typography.h1
        )
        //
    //
    }
}