package com.example.cardiocopy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
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

enum class EstadoEntrega(val texto: String, val color: Color) {
    PENDIENTE("Pendiente de entrega", Color(0xFFD08A1E)),
    ENTREGADO("Entregado", Color(0xFF5E9E7A))
}

// Resumen de un registro para la lista. No contiene datos personales.
data class RegistroResumen(
    val numero: Int,
    val fecha: String,          // formato "dd/MM/yyyy · HH:mm"
    val soplos: Int,
    val estado: EstadoEntrega = EstadoEntrega.PENDIENTE
) {
    val hora: String get() = fecha.substringAfter("· ").trim()
    val dia: String get() = fecha.substringBefore("·").trim()
}

// Datos de ejemplo (luego vendrán de la base de datos)
val registrosEjemplo = listOf(
    RegistroResumen(3, "09/11/2026 · 15:30", 2, EstadoEntrega.PENDIENTE),
    RegistroResumen(2, "02/11/2026 · 10:15", 0, EstadoEntrega.ENTREGADO),
    RegistroResumen(1, "25/10/2026 · 18:40", 1, EstadoEntrega.ENTREGADO)
)

@Composable
fun HistorialScreen(
    onBack: () -> Unit,
    onAbrir: (numero: Int) -> Unit,
    registros: List<RegistroResumen> = registrosEjemplo
) {
    var busqueda by remember { mutableStateOf("") }

    val filtrados = registros.filter {
        val q = busqueda.trim()
        q.isBlank() ||
                it.numero.toString().contains(q) ||
                it.fecha.contains(q) ||
                it.estado.texto.contains(q, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondo)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AzulOscuro)
            }
            Text("HISTORIAL", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar por hora, fecha o estado") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        if (filtrados.isEmpty()) {
            Spacer(Modifier.height(32.dp))
            Text(
                if (registros.isEmpty()) "Aún no hay registros." else "No se encontraron registros.",
                color = AzulOscuro,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filtrados, key = { it.numero }) { reg ->
                    TarjetaRegistro(reg) { onAbrir(reg.numero) }
                }
            }
        }
    }
}

@Composable
private fun TarjetaRegistro(reg: RegistroResumen, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                "Registro realizado a las ${reg.hora}",
                fontWeight = FontWeight.Bold,
                color = AzulOscuro
            )
            Text(reg.dia, fontSize = 13.sp, color = AzulBoton)

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    when (reg.soplos) {
                        0 -> "Normal"
                        1 -> "1 soplo detectado"
                        else -> "${reg.soplos} soplos detectados"
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (reg.soplos == 0) VerdeBoton else Rojo
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    reg.estado.texto,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = reg.estado.color,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(reg.estado.color.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AzulOscuro)
    }
}