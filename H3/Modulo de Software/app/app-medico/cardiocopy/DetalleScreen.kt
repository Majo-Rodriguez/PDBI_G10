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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class HallazgoFoco(
    val numero: Int,
    val nombreFoco: String,
    val color: Color,
    val soplo: String?
)


fun hallazgosDeEjemplo(numero: Int): List<HallazgoFoco> {
    val soplos: Map<Int, String> = when (numero) {
        3 -> mapOf(
            3 to "Soplo sistólico en la válvula tricúspide",
            4 to "Soplo diastólico en la válvula mitral"
        )
        1 -> mapOf(1 to "Soplo sistólico en la válvula aórtica")
        else -> emptyMap() // registro 2: normal
    }
    return listOf(
        HallazgoFoco(1, "Foco Aórtico", Color(0xFFF9C6D3), soplos[1]),
        HallazgoFoco(2, "Foco Pulmonar", Color(0xFFD5F0A8), soplos[2]),
        HallazgoFoco(3, "Foco Tricúspide", Color(0xFFFCEB5C), soplos[3]),
        HallazgoFoco(4, "Foco Mitral / Apexiano", Color(0xFFF9B97C), soplos[4])
    )
}

@Composable
fun DetalleScreen(
    numeroRegistro: Int = 1,
    fecha: String = "09/11/2026 · 15:30",
    hallazgos: List<HallazgoFoco> = hallazgosDeEjemplo(numeroRegistro),
    onBack: () -> Unit,
    onEntregar: (() -> Unit)? = null,
    onExportar: () -> Unit = {}
) {
    val conSoplo = hallazgos.filter { it.soplo != null }

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
            Text("DETALLE DE REGISTRO", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Nº Registro $numeroRegistro", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
                Text(fecha, fontSize = 13.sp, color = AzulBoton)

                Spacer(Modifier.height(16.dp))

                Text("Detalle:", fontWeight = FontWeight.Bold, color = AzulOscuro)
                Spacer(Modifier.height(8.dp))

                if (conSoplo.isEmpty()) {
                    FilaResultado(Color(0xFFD5F0A8), "No se detectaron soplos en los 5 focos", true)
                } else {
                    conSoplo.forEach { h ->
                        FilaResultado(h.color, h.soplo ?: "", false, "Foco ${h.numero}: ${h.nombreFoco}")
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Resumen por foco", fontWeight = FontWeight.Bold, color = AzulOscuro)
                Spacer(Modifier.height(8.dp))
                hallazgos.forEach { h ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(26.dp).clip(CircleShape).background(h.color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${h.numero}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(h.nombreFoco, color = AzulOscuro, modifier = Modifier.weight(1f))
                        Text(
                            if (h.soplo == null) "Normal" else "Soplo",
                            fontWeight = FontWeight.Medium,
                            color = if (h.soplo == null) VerdeBoton else Rojo
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        if (onEntregar != null) {
            Button(
                onClick = onEntregar,
                colors = ButtonDefaults.buttonColors(containerColor = AzulBoton),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("ENTREGAR AL PACIENTE", fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = onExportar,
            colors = ButtonDefaults.buttonColors(containerColor = VerdeBoton),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("EXPORTAR REPORTE", fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Este resultado es orientativo y no reemplaza la evaluación de un profesional de la salud.",
            fontSize = 11.sp,
            color = AzulOscuro
        )
    }
}

@Composable
private fun FilaResultado(
    color: Color,
    texto: String,
    esNormal: Boolean,
    titulo: String? = null
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            Modifier.size(28.dp).clip(CircleShape).background(color),
            contentAlignment = Alignment.Center
        ) {
            if (esNormal) {
                Icon(Icons.Default.Check, contentDescription = null, tint = AzulOscuro, modifier = Modifier.size(16.dp))
            }
        }
        Spacer(Modifier.width(12.dp))
        Column {
            if (titulo != null) {
                Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AzulBoton)
            }
            Text(texto, color = AzulOscuro)
        }
    }
}
