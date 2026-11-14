package com.example.cardiocopy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

val AzulFondo = Color(0xFFDCE9F5)
val AzulOscuro = Color(0xFF2F4A6D)
val AzulBoton = Color(0xFF5B86B0)
val VerdeBoton = Color(0xFF5E9E7A)
val Rojo = Color(0xFFD9534F)

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondo),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Rojo, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.Hearing, contentDescription = null, tint = AzulOscuro, modifier = Modifier.size(96.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text("CARDIOCORI", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
            Text("DETECTOR DE SOPLOS", fontSize = 12.sp, color = AzulOscuro)
        }
    }
}