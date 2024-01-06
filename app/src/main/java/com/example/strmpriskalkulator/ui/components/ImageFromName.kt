package com.example.strmpriskalkulator.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
//This function is used to show the weather symbols on the screen given the image name.
@Composable
fun ImageFromName(imageName: String, modifier: Modifier = Modifier, imageDescription: String ) {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
    val painter = painterResource(id = resourceId)

    // Can crash here:
    
    Image(
        painter = painter,
        contentDescription = imageDescription,
        modifier = modifier
    )
}