package com.example.strmpriskalkulator.ui.screens.testscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strmpriskalkulator.R
import com.example.strmpriskalkulator.ui.components.BottomBar
import com.example.strmpriskalkulator.ui.components.TopBar
import com.example.strmpriskalkulator.viewmodel.StromprisViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(navController: NavController, stromPrisViewModel: StromprisViewModel = viewModel()) {

    val priceDistricts = stringArrayResource(id = R.array.price_districts)
    val priceDistrictsDescription = stringArrayResource(id = R.array.price_districts_description)

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {TopBar(navController, "Innstillinger")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                /*modifier = Modifier.menuAnchor(),*/
                readOnly = true,
                value = stromPrisViewModel.priceDistrict,
                onValueChange = {},
                label = { Text("Prisområder") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(colors.onPrimary),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                priceDistricts.forEachIndexed { index, district ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            stromPrisViewModel.changePriceDistrict(district)
                        }
                    ) {
                        Text(text = "$district (${priceDistrictsDescription[index]})")
                    }
                }
            }
        }
        BottomBar(navController = navController)
    }
}

