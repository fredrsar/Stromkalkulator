package com.example.strmpriskalkulator.ui.screens.testscreen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strmpriskalkulator.R
import com.example.strmpriskalkulator.ui.components.BottomBar
import com.example.strmpriskalkulator.ui.components.ImageFromName
import com.example.strmpriskalkulator.ui.components.ProgressBarStrompris
import com.example.strmpriskalkulator.ui.components.TopBar
import com.example.strmpriskalkulator.ui.theme.Typography
import com.example.strmpriskalkulator.viewmodel.StromprisViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun MainScreen(navController: NavController, stromPrisViewModel: StromprisViewModel = viewModel()){
    val uiState by stromPrisViewModel.homeUiState.collectAsState()
    val format = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale("no", "NO"))
    val formattedDate = format.format(uiState.date)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        TopBar(navController, " ") // Innholdet til TopBar kan endres her

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly            ) {

                // Overskrift
                Text(
                    text = "Spotpris",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                )

                // Stygg data for å oppdatere sted-taggen ut fra hva man velger i meny
                val priceDistrictNames : Array<String> = stringArrayResource(id = R.array.price_districts_description)
                val priceDistricts: Array<String> = stringArrayResource(id = R.array.price_districts)
                var districtName = "Oslo"

                priceDistricts. forEachIndexed { index, it ->
                    if (it == stromPrisViewModel.priceDistrict) {
                        districtName =  priceDistrictNames[index]
                    }
                }

                // Viser sted-tag
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "$districtName, Norge",
                    style = Typography.body1
                )

                // Avstand mellom sted-tag og dato
                Spacer(Modifier.height(2.dp))
                // Viser dato
                Text(
                    text =  formattedDate,
                )
                Spacer(modifier = Modifier.height(25.dp))
                ProgressBarStrompris(
                    text = "${(uiState.currentPrice * 100.0).roundToInt() / 100.0}\nkr/kWh",
                    progress = uiState.precentageOfHighestDayPrice.toFloat()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = " ${(uiState.precentageOfHighestDayPrice *100.0).roundToInt()}% av maks pris i dag\n " +
                        "${(uiState.percentageComparison * 100.0).roundToInt() / 100.0}% endring fra gjennomsnitt i går",
                    textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(40.dp))
                Card(
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .height(150.dp)
                        .width(310.dp)
                        .padding(16.dp),
                    backgroundColor = colors.surface,
                    elevation = 5.dp,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ImageFromName(
                                imageName = uiState.weatherSymbolCode,
                                modifier = Modifier
                                    .height(70.dp)
                                    .width(70.dp),
                                imageDescription = "image of weather"
                            )
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "${uiState.currentTemp}°",
                                    fontSize = 30.sp,
                                    style = typography.body1,
                                    modifier = Modifier.offset(y = (-10).dp),
                                )
                                Text(
                                    text = "Oslo, Norge",
                                    style = typography.body1,
                                    modifier = Modifier.offset(y = (-6).dp),
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
            }
        BottomBar(navController = navController)

    }
}

