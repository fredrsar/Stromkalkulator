package com.example.strmpriskalkulator.ui.screens.testscreen


import android.annotation.SuppressLint

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strmpriskalkulator.R
import com.example.strmpriskalkulator.ui.components.BottomBar
import com.example.strmpriskalkulator.ui.components.TopBar
import com.example.strmpriskalkulator.viewmodel.StromprisViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PricePerActivityScreen(navController: NavController, stromPrisViewModel: StromprisViewModel = viewModel()) {
    val uiState by stromPrisViewModel.pricePerActivityUiState.collectAsState()

    val activities = listOf(
        Activity("Dusj", "Pleie", 6.0, "10 min, 160 liter vann = ~6 kWh",  R.drawable.shower),
        Activity("Ovn hele dagen", "Husholdning", 24.0, "1000W x 24 timer = 24 kWh", R.drawable.ovn),
        Activity("Koke 1 liter vann", "Mat", 0.12, "Kokeplate i 4 min = ~0,12 kWh", R.drawable.water),
        Activity("Steke en grandiosa", "Mat", 1.1, "Stekeovn i 30 min = ~1,1 kWh", R.drawable.pizza),
        Activity("Lade elbilen", "Elektronikk og kjøretøy", 45.0, "Nissan Leaf 10-80% = ~45 kWh", R.drawable.car),
        Activity("TV en time", "Elektronikk og kjøretøy", 0.06, "Snittforbruk = ~0,06 kWh", R.drawable.tv),
        Activity("En vask", "Husholdning", 0.8, "Snittforbruk = ~0,8 kWh", R.drawable.wash),
        Activity("Lade mobil", "Elektronikk og kjøretøy", 0.005, "Snittforbruk = ~0,005 kWh", R.drawable.charge),
        Activity("Kjøleskapet per døgn", "Husholdning", 0.44, "Snittforbruk = ~0,44 kWh", R.drawable.fridge),
        Activity("Støvsuge", "Husholdning", 0.33, "Støvsuger i 10 min = ~0,33 kWh", R.drawable.clean),
        Activity("Hårføner", "Pleie", 0.33, "Hårføner i 10 min = ~0,33 kWh", R.drawable.hair),
    )
    val categories = listOf("Alle") + activities.map { it.category }.distinct()
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    Scaffold(
        topBar = {
            TopBar(navController = navController, screenTitle = "Forbruk - hvor mye koster ting nå?")
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        Column {
            LazyRow(Modifier.padding(start = 12.dp)) {
            items(categories) { category ->
                    CategoryChip(category = category, selectedCategory = selectedCategory, onCategorySelected = { selectedCategory = it })
                }
            }

            ActivitiesPrice(
                spotPrice = uiState.spotPrice,
                activitiesList = if (selectedCategory == "Alle") activities else activities.filter { it.category == selectedCategory }
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryChip(category: String, selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val isSelected = category == selectedCategory
    val backgroundColor = if (isSelected) colors.primary else colors.surface
    val contentColor = if (isSelected) colors.background else colors.onSurface

    Chip(
        modifier = Modifier.padding(end = 8.dp),
        onClick = { onCategorySelected(category) },
        border = ChipDefaults.outlinedBorder,
        colors = ChipDefaults.outlinedChipColors(contentColor = contentColor, backgroundColor = backgroundColor),
    ) {
        Text(category)
    }
}

@Composable
fun ActivitiesPrice(spotPrice: Double, activitiesList: List<Activity>) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 130.dp)
    ) {
        items(activitiesList) { activity ->
            ActivityCard(activity = activity, spotPrice = spotPrice)
        }
    }
}

@Composable
fun ActivityCard(activity: Activity, spotPrice: Double) {
    val activityPrice = activity.energy * spotPrice
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .height(200.dp)
            .width(400.dp)
            .padding(16.dp),
        backgroundColor = colors.surface,
        elevation = 5.dp,

        ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = activity.iconResId),
                contentDescription = activity.name,
                modifier = Modifier
                    .weight(1f)
                    .size(80.dp),
            )
            Box(modifier = Modifier.weight(2f)) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = activity.name, textAlign = TextAlign.Center, fontSize =  30.sp, style = typography.h1)
                    Text(text = "${String.format("%.2f", activityPrice)} kr", fontSize =  20.sp, textAlign = TextAlign.Center, style = typography.body2, modifier = Modifier.offset(y = (-5).dp))
                    Text(text = activity.description, textAlign = TextAlign.Center, style = typography.body1, modifier = Modifier.offset(y = (-10).dp))
                }
            }
        }
    }
}

data class Activity (val name: String, val category: String, val energy: Double, val description: String, val iconResId: Int )