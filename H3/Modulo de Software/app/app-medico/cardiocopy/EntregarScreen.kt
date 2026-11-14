package com.example.cardiocopy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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

// Duración del código de entrega, en segundos (5 minutos)
private const val DURACION_CODIGO = 300

@Composable
fun EntregarScreen(
    numeroRegistro: Int,
    onBack: () -> Unit,
    onTerminar: () -> Unit,
    onEntregado: () -> Unit = {}
) {
    // SIMULACIÓN: más adelante el código se generará y validará por Bluetooth.
    var intento by remember { mutableIntStateOf(0) }
    val codigo = remember(intento) { (0..999999).random().toString().padStart(6, '0') }
    var restante by remember { mutableIntStateOf(DURACION_CODIGO) }
    var entregado by remember { mutableStateOf(false) }

    LaunchedEffect(intento) {
        restante = DURACION_CODIGO
        while (restante > 0) {
            delay(1000)
            restante--
        }
    }

    val vencido = restante <= 0 && !entregado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondo)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AzulOscuro)
            }
            Text("ENTREGAR RESULTADO", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
        }

        Spacer(Modifier.height(32.dp))

        if (entregado) {
            Box(
                modifier = Modifier.size(72.dp).clip(CircleShape).background(VerdeBoton),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("Registro entregado", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
            Spacer(Modifier.height(8.dp))
            Text(
                "El paciente ya tiene su resultado en su app.",
                textAlign = TextAlign.Center,
                color = AzulOscuro
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onTerminar,
                colors = ButtonDefaults.buttonColors(containerColor = AzulBoton),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("VOLVER AL MENÚ", fontWeight = FontWeight.Medium)
            }
        } else {
            Text(
                "Indique este código al paciente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = AzulOscuro
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Debe ingresarlo en su app para recibir su registro.",
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = AzulOscuro
            )

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                codigo.forEach { digito ->
                    Box(
                        modifier = Modifier
                            .size(width = 44.dp, height = 58.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (vencido) Color(0xFFDDE3EA) else Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            digito.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (vencido) Color(0xFF9AA7B5) else AzulOscuro
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (vencido) {
                Text("Código vencido", fontWeight = FontWeight.Bold, color = Rojo)
            } else {
                Text(
                    "Vence en %d:%02d".format(restante / 60, restante % 60),
                    color = AzulOscuro
                )
            }

            Spacer(Modifier.height(4.dp))
            Text("Sirve una sola vez.", fontSize = 12.sp, color = AzulOscuro)

            Spacer(Modifier.height(24.dp))

            if (vencido) {
                Button(
                    onClick = { intento++ },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulBoton),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("GENERAR NUEVO CÓDIGO", fontWeight = FontWeight.Medium)
                }
            } else {
                // Solo para pruebas: reemplaza la validación real del paciente.
                OutlinedButton(
                    onClick = {
                        entregado = true
                        onEntregado()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("SIMULAR ENTREGA (solo pruebas)", color = AzulOscuro)
                }
            }
        }
    }
}