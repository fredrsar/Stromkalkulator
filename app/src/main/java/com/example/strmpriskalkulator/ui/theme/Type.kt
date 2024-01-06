package com.example.strmpriskalkulator.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.strmpriskalkulator.R

val RalewayFont = FontFamily(
    Font(R.font.relay_raleway_black, FontWeight.Black),
    Font(R.font.relay_raleway_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.relay_raleway_bold, FontWeight.Bold),
    Font(R.font.relay_raleway_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.relay_raleway_extrabold, FontWeight.ExtraBold),
    Font(R.font.relay_raleway_extrabold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.relay_raleway_extralight, FontWeight.ExtraLight),
    Font(R.font.relay_raleway_extralight_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.relay_raleway_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.relay_raleway_light, FontWeight.Light),
    Font(R.font.relay_raleway_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.relay_raleway_medium, FontWeight.Medium),
    Font(R.font.relay_raleway_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.relay_raleway_regular, FontWeight.Normal),
    Font(R.font.relay_raleway_semibold, FontWeight.SemiBold),
    Font(R.font.relay_raleway_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.relay_raleway_thin, FontWeight.Thin),
    Font(R.font.relay_raleway_thin_italic, FontWeight.Thin, FontStyle.Italic)
)


val Typography = Typography(
    body1 = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp

    ),
    body2 = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    h2 = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp
    )
)



