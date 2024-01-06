package com.example.strmpriskalkulator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.strmpriskalkulator.R

//This is to have a component for the bottom of the screen for all screens.
@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hjemIcon = painterResource(R.drawable.home)
    val detaljerIcon = painterResource(R.drawable.detaljert)
    val beregnIcon = painterResource(R.drawable.kalkulator)

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.background(
            colors.secondaryVariant)
            .border(width = 1.dp, color = colors.primaryVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,

        ) {
            //Spacer(modifier = Modifier.width(1.dp))
            RoundButton(hjemIcon, 50, "Hjem", currentRoute == "main") {
                navController.navigate("main")
            }
            Spacer(modifier = Modifier.width(1.dp))
            RoundButton(detaljerIcon, 50, "Detaljer", currentRoute == "detaljert") {
                navController.navigate("detaljert")
            }
            Spacer(modifier = Modifier.width(1.dp))

            RoundButton(beregnIcon, 50, "Forbruk", currentRoute == "forbruk") {
                navController.navigate("forbruk")
            }
        }
    }
}