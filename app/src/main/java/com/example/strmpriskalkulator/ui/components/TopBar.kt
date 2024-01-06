package com.example.strmpriskalkulator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.strmpriskalkulator.R

@Composable
fun TopBar(navController: NavController, screenTitle: String){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val settingIcon = painterResource(R.drawable.settings)
    Row(
        modifier = Modifier.fillMaxWidth().padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        //Innhold i topBar

        Text(
            screenTitle,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        RoundButton(settingIcon, 30, " ", currentRoute == "settings") {
            navController.navigate("settings")
        }
    }
}