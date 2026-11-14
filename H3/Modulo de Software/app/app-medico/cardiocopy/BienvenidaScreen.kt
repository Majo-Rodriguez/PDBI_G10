package com.example.cardiocopy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private enum class EstadoConexion { DESCONECTADO, BUSCANDO, ENCONTRADO, CONECTADO }

private const val NOMBRE_ESP32 = "CardioCori-ESP32"

@Composable
fun BienvenidaScreen(
    onVincular: () -> Unit = {},
    onContinuar: () -> Unit
) {
    
    var estado by remember { mutableStateOf(EstadoConexion.DESCONECTADO) }

    
    LaunchedEffect(estado) {
        if (estado == EstadoConexion.BUSCANDO) {
            delay(2000)
            estado = EstadoConexion.ENCONTRADO
        }
    }

    val conectado = estado == EstadoConexion.CONECTADO

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        Text("NUEVO REGISTRO", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)

        Spacer(Modifier.height(16.dp))

        
        Icon(
            Icons.Default.Hearing,
            contentDescription = null,
            tint = AzulOscuro,
            modifier = Modifier.size(80.dp)
        )

        Spacer(Modifier.height(32.dp))

        Text("Dispositivo vinculado:", fontSize = 16.sp, color = AzulOscuro)

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (estado) {
                    EstadoConexion.DESCONECTADO -> {
                        Text(
                            "No hay ningún dispositivo vinculado",
                            textAlign = TextAlign.Center,
                            color = AzulOscuro
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                estado = EstadoConexion.BUSCANDO
                                onVincular()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AzulBoton),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("VINCULAR DISPOSITIVO")
                        }
                    }

                    EstadoConexion.BUSCANDO -> {
                        CircularProgressIndicator(color = AzulBoton, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Buscando dispositivos...", color = AzulOscuro)
                    }

                    EstadoConexion.ENCONTRADO -> {
                        Text("Dispositivos encontrados", fontWeight = FontWeight.Bold, color = AzulOscuro)
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(AzulFondo)
                                .clickable { estado = EstadoConexion.CONECTADO }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Bluetooth, contentDescription = null, tint = AzulBoton)
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(NOMBRE_ESP32, fontWeight = FontWeight.Medium, color = AzulOscuro)
                                Text("Toque para conectar", fontSize = 12.sp, color = AzulBoton)
                            }
                        }
                        TextButton(onClick = { estado = EstadoConexion.BUSCANDO }) {
                            Text("Buscar de nuevo", color = AzulBoton)
                        }
                    }

                    EstadoConexion.CONECTADO -> {
                        Box(
                            Modifier.size(40.dp).clip(CircleShape).background(VerdeBoton),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Conectado a $NOMBRE_ESP32", fontWeight = FontWeight.Medium, color = AzulOscuro)
                        TextButton(onClick = { estado = EstadoConexion.DESCONECTADO }) {
                            Text("Desvincular", color = Rojo)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        if (!conectado) {
            Text(
                "Vincule el dispositivo para continuar",
                fontSize = 12.sp,
                color = AzulOscuro
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = onContinuar,
            enabled = conectado,
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulBoton,
                disabledContainerColor = Color(0xFFB8C7D6),
                disabledContentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CONTINUAR")
        }

        Spacer(Modifier.height(16.dp))
    }
}
