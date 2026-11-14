package com.example.cardiocopy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class Foco(
    val numero: Int,
    val nombre: String,
    val color: Color,
    val ubicacion: String,
    val lado: String,
    val valvula: String,
    val detecta: String
)

private val focos = listOf(
    Foco(
        1, "Foco Aórtico", Color(0xFFF9C6D3),
        "2.º espacio intercostal, junto al esternón",
        "Derecho",
        "Válvula aórtica",
        "Soplos de estenosis aórtica"
    ),
    Foco(
        2, "Foco Pulmonar", Color(0xFFD5F0A8),
        "2.º espacio intercostal, junto al esternón",
        "Izquierdo",
        "Válvula pulmonar",
        "Soplos de estenosis pulmonar"
    ),
    Foco(
        3, "Foco Tricúspide", Color(0xFFFCEB5C),
        "4.º–5.º espacio intercostal, junto al esternón",
        "Izquierdo",
        "Válvula tricúspide",
        "Soplos de insuficiencia tricúspide"
    ),
    Foco(
        4, "Foco Mitral / Apexiano", Color(0xFFF9B97C),
        "5.º espacio intercostal, línea medioclavicular (ápex)",
        "Izquierdo",
        "Válvula mitral",
        "Soplos de insuficiencia o estenosis mitral"
    )
)

@Composable
fun GuiaScreen(onBack: () -> Unit) {
    var seleccionado by remember { mutableStateOf<Foco?>(null) }

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
            Text(
                "GUÍA DE MANEJO",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AzulOscuro
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Focos de auscultación",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = AzulOscuro
        )

        Spacer(Modifier.height(16.dp))

        Image(
            painter = painterResource(R.drawable.torax),
            contentDescription = "Focos de auscultación en el tórax",
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            contentScale = ContentScale.FillWidth
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Toca un foco para ver el detalle",
            fontSize = 12.sp,
            color = AzulOscuro
        )

        Spacer(Modifier.height(12.dp))

        focos.forEach { foco ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { seleccionado = foco }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(foco.color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        foco.numero.toString(),
                        color = AzulOscuro,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    foco.nombre,
                    fontWeight = FontWeight.Medium,
                    color = AzulOscuro,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AzulOscuro
                )
            }
        }
    }

    // Ventana de detalle
    seleccionado?.let { foco ->
        AlertDialog(
            onDismissRequest = { seleccionado = null },
            containerColor = Color.White,
            confirmButton = {
                TextButton(onClick = { seleccionado = null }) {
                    Text("Cerrar", color = AzulBoton)
                }
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(foco.color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        foco.numero.toString(),
                        fontSize = 20.sp,
                        color = AzulOscuro,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            title = {
                Text(foco.nombre, color = AzulOscuro, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetalleItem("Ubicación", foco.ubicacion)
                    DetalleItem("Lado", foco.lado)
                    DetalleItem("Válvula", foco.valvula)
                    DetalleItem("Detecta", foco.detecta)
                }
            }
        )
    }
}

@Composable
private fun DetalleItem(titulo: String, texto: String) {
    Column {
        Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AzulBoton)
        Text(texto, fontSize = 15.sp, color = AzulOscuro)
    }
}