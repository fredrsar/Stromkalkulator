package com.example.strmpriskalkulator.ui.screens.testscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strmpriskalkulator.ui.components.BottomBar
import com.example.strmpriskalkulator.ui.components.StromprisGraf
import com.example.strmpriskalkulator.ui.components.TopBar
import com.example.strmpriskalkulator.viewmodel.StromprisViewModel
import kotlin.math.roundToInt

/* This screen shows detailed prices, either for this week, month or shows the next weeks predicted prices which the user can choose.
* */

@Composable
fun DetaljertPrisoversiktScreen(navController: NavController, stromPrisViewModel: StromprisViewModel = viewModel()) {
    val uiState by stromPrisViewModel.detailedPriceUiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(navController, "Detaljert prisoversikt")
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text("Spotpris", style = typography.h1, modifier = Modifier.align(Alignment.Start))
                    Text("${(uiState.currentPrice * 100.0).roundToInt() / 100.0} kr/kWh", style = typography.h2, modifier = Modifier.align(Alignment.Start))
                    Spacer(Modifier.height(15.dp))
                    val periodDescription = when (uiState.graphOptions) {
                        GraphOptions.Last7Days -> "siste 7 dager"
                        GraphOptions.Last30Days -> "siste 30 dager"
                        GraphOptions.Prediction7 -> ""
                    }
                    if (uiState.graphOptions != GraphOptions.Prediction7) {
                        Text("Prisendring", style = typography.h1,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Text(
                            (if (uiState.percentageLastXDays >= 0) "+" else "") +
                                    "${(uiState.percentageLastXDays * 100.0).roundToInt() / 100.0}%, " +
                                    periodDescription,
                            color =
                            if (uiState.percentageLastXDays >= 0)
                                Color(0xFFD62000)
                            else Color(0xFF5A822B), // specific color-codes for universal design
                            style = typography.body1,
                            fontSize = 18.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                }
                Spacer(Modifier.height(50.dp))
                StromprisGraf(stromdata = uiState.avgELPriceXDays)

                Spacer(Modifier.height(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    // shows button of the day-interval selected
                    when (uiState.graphOptions) {
                        GraphOptions.Last7Days -> {
                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(GraphOptions.Last7Days)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Siste 7 dager", color = colors.primary, style = typography.h1)
                            }
                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(graphOption = GraphOptions.Last30Days)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Siste 30 dager", color =colors.onPrimary, style = typography.body1)
                            }
                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(graphOption = GraphOptions.Prediction7)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Predikering",color =colors.onPrimary, style = typography.body1)
                            }
                        }
                        GraphOptions.Last30Days -> {

                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(graphOption = GraphOptions.Last7Days)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Siste 7 dager", color = colors.onSurface, style = typography.body1)
                            }
                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(graphOption = GraphOptions.Last30Days)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Siste 30 dager", color = colors.primary, style = typography.h1)
                            }
                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(graphOption = GraphOptions.Prediction7)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Predikering", color = colors.onSurface, style = typography.body1)
                            }
                        }
                        else -> {
                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(graphOption = GraphOptions.Last7Days)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Siste 7 dager", color = colors.onSurface, style = typography.body1)
                            }
                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(graphOption = GraphOptions.Last30Days)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Siste 30 dager", color = colors.onSurface, style = typography.body1)
                            }
                            TextButton(
                                colors = ButtonDefaults.buttonColors(contentColor = colors.onSurface, backgroundColor = colors.surface),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (uiState.graphButtonActive) {
                                        stromPrisViewModel.changeTimeIntervalGraph(graphOption = GraphOptions.Prediction7)
                                    }
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = "Predikering", color = colors.primary, style = typography.h1)
                            }
                        }
                    }
                }
            }
        }
        BottomBar(navController = navController)
    }
}



