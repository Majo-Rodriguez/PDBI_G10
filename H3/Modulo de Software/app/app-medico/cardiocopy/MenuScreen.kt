package com.example.cardiocopy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    onNuevoRegistro: () -> Unit,
    onHistorial: () -> Unit,
    onGuia: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(72.dp))

        Text("BIENVENIDO", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)

        Spacer(Modifier.height(16.dp))

        // Logo (res/drawable/logo.jpg)
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo Cardiocori",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(24.dp))

        Text(
            "Seleccione una opción",
            fontSize = 18.sp,
            color = AzulOscuro
        )

        Spacer(Modifier.height(32.dp))

        MenuButton("NUEVO REGISTRO", onNuevoRegistro)
        Spacer(Modifier.height(16.dp))
        MenuButton("HISTORIAL", onHistorial)
        Spacer(Modifier.height(16.dp))
        MenuButton("GUÍA DE MANEJO", onGuia)
    }
}

@Composable
fun MenuButton(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = AzulOscuro
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(texto, fontWeight = FontWeight.Medium)
    }
}