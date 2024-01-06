package com.example.strmpriskalkulator


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strmpriskalkulator.data.DataSource
import com.example.strmpriskalkulator.data.Repository
import com.example.strmpriskalkulator.ui.screens.testscreen.DetaljertPrisoversiktScreen
import com.example.strmpriskalkulator.ui.screens.testscreen.MainScreen
import com.example.strmpriskalkulator.ui.screens.testscreen.PricePerActivityScreen
import com.example.strmpriskalkulator.ui.screens.testscreen.SettingsScreen
import com.example.strmpriskalkulator.ui.theme.ColorBackground
import com.example.strmpriskalkulator.ui.theme.StromprisKalkulatorTheme
import com.example.strmpriskalkulator.viewmodel.StromprisViewModel
import com.example.strmpriskalkulator.ui.screens.testscreen.NoInternetConnectionScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StromprisKalkulatorTheme {
                if ( isOnline(this) )
                {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = ColorBackground
                    ) {
                        val navController = rememberNavController()
                        val viewModel = StromprisViewModel(
                            repository = Repository(
                                DataSource("https://www.hvakosterstrommen.no/api/v1/prices/"),
                                DataSource("https://gw-uio.intark.uh-it.no/in2000/weatherapi//locationforecast/2.0/compact?lat=59.9114&lon=10.7579")
                            )
                        )
                        StromprisNavHost(navController, viewModel)
                    }
                } else {
                    NoInternetConnectionScreen()
                }
            }
        }
    }
}
@Composable
fun StromprisNavHost(navController: NavHostController, viewModel: StromprisViewModel) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController, viewModel) }
        composable("detaljert") { DetaljertPrisoversiktScreen(navController, viewModel) }
        composable("settings") { SettingsScreen(navController, viewModel) }
        composable("forbruk") { PricePerActivityScreen(navController = navController, stromPrisViewModel = viewModel)}
    }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}