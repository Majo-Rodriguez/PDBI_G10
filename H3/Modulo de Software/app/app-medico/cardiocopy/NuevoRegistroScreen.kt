package com.example.cardiocopy

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.exp
import kotlin.math.pow

private enum class Paso { POSICIONAR, VERIFICAR, GRABANDO, DECIDIR, FINAL }

private data class FocoPaso(
    val nombre: String,
    val ubicacion: String,
    val color: Color
)

private val focosPaso = listOf(
    FocoPaso("Foco Aórtico", "2.º espacio intercostal, derecho, junto al esternón", Color(0xFFF9C6D3)),
    FocoPaso("Foco Pulmonar", "2.º espacio intercostal, izquierdo, junto al esternón", Color(0xFFD5F0A8)),
    FocoPaso("Foco Tricúspide", "4.º–5.º espacio intercostal, izquierdo, junto al esternón", Color(0xFFFCEB5C)),
    FocoPaso("Foco Mitral / Apexiano", "5.º espacio intercostal, línea medioclavicular", Color(0xFFF9B97C))
)

private const val SEGUNDOS_GRABACION = 5

@Composable
fun NuevoRegistroScreen(
    onBack: () -> Unit,
    onTerminar: () -> Unit
) {
    var paso by remember { mutableStateOf(Paso.POSICIONAR) }
    var focoIdx by remember { mutableIntStateOf(0) }
    var segundos by remember { mutableIntStateOf(SEGUNDOS_GRABACION) }

    // Cuenta regresiva de la grabación
    LaunchedEffect(paso, focoIdx) {
        if (paso == Paso.GRABANDO) {
            segundos = SEGUNDOS_GRABACION
            while (segundos > 0) {
                delay(1000)
                segundos--
            }
            paso = Paso.DECIDIR
        }
    }

    val completados = if (paso == Paso.FINAL) focosPaso.size else focoIdx
    val foco = focosPaso[focoIdx.coerceIn(0, focosPaso.size - 1)]

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
            Text("NUEVO REGISTRO", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
        }

        Spacer(Modifier.height(16.dp))

        ProgresoFocos(completados = completados, actual = focoIdx, terminado = paso == Paso.FINAL)
        Spacer(Modifier.height(16.dp))

        when (paso) {
            Paso.POSICIONAR -> {
                TarjetaFoco(foco, focoIdx)
                Spacer(Modifier.height(16.dp))
                Text(
                    "Posicione la campana ",
                    textAlign = TextAlign.Center,
                    color = AzulOscuro
                )
                Spacer(Modifier.height(24.dp))
                BotonPrincipal("Listo") { paso = Paso.VERIFICAR }
            }

            Paso.VERIFICAR -> {
                TarjetaFoco(foco, focoIdx)
                Spacer(Modifier.height(16.dp))
                SenalSimulada(Modifier.fillMaxWidth().height(160.dp))
                Spacer(Modifier.height(12.dp))
                Text("¿Se observa correctamente la señal PCG?", color = AzulOscuro, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                BotonPrincipal("Tomar medicion", color = VerdeBoton) { paso = Paso.GRABANDO }
            }

            Paso.GRABANDO -> {
                TarjetaFoco(foco, focoIdx)
                Spacer(Modifier.height(16.dp))
                SenalSimulada(Modifier.fillMaxWidth().height(160.dp))
                Spacer(Modifier.height(16.dp))
                Text("Grabando... ${segundos}s", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = AzulOscuro)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (SEGUNDOS_GRABACION - segundos) / SEGUNDOS_GRABACION.toFloat() },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = AzulBoton,
                    trackColor = Color.White
                )
            }

            Paso.DECIDIR -> {
                TarjetaFoco(foco, focoIdx)
                Spacer(Modifier.height(16.dp))
                SenalSimulada(Modifier.fillMaxWidth().height(160.dp))
                Spacer(Modifier.height(12.dp))
                Text("¿Guardar señal?", fontSize = 18.sp, color = AzulOscuro)
                Spacer(Modifier.height(16.dp))
                BotonPrincipal("GUARDAR SEÑAL", color = VerdeBoton) {
                    if (focoIdx == focosPaso.size - 1) {
                        paso = Paso.FINAL
                    } else {
                        focoIdx++
                        paso = Paso.POSICIONAR
                    }
                }
                Spacer(Modifier.height(8.dp))
                BotonPrincipal("REPETIR GRABACIÓN", color = Rojo) { paso = Paso.GRABANDO }
            }

            Paso.FINAL -> {
                Spacer(Modifier.height(32.dp))
                Box(
                    modifier = Modifier.size(72.dp).clip(CircleShape).background(VerdeBoton),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Se guardaron los 5 puntos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
                Spacer(Modifier.height(8.dp))
                Text("Retire la campana del paciente", color = AzulOscuro)
                Spacer(Modifier.height(24.dp))
                BotonPrincipal("VER RESULTADO") { onTerminar() }
            }
        }
    }
}

@Composable
private fun ProgresoFocos(completados: Int, actual: Int, terminado: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        focosPaso.forEachIndexed { i, f ->
            val hecho = i < completados
            val esActual = i == actual && !terminado
            Box(
                modifier = Modifier
                    .size(if (esActual) 40.dp else 32.dp)
                    .clip(CircleShape)
                    .background(if (hecho || esActual) f.color else Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (hecho) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = AzulOscuro, modifier = Modifier.size(18.dp))
                } else {
                    Text("${i + 1}", color = AzulOscuro, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TarjetaFoco(foco: FocoPaso, idx: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(foco.color),
                contentAlignment = Alignment.Center
            ) {
                Text("${idx + 1}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Foco ${idx + 1} de ${focosPaso.size}", fontSize = 12.sp, color = AzulBoton, fontWeight = FontWeight.Bold)
                Text(foco.nombre, fontWeight = FontWeight.Bold, color = AzulOscuro)
                Text(foco.ubicacion, fontSize = 12.sp, color = AzulOscuro)
            }
        }
    }
}

@Composable
private fun BotonPrincipal(
    texto: String,
    color: Color = AzulBoton,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.fillMaxWidth().height(48.dp)
    ) {
        Text(texto, fontWeight = FontWeight.Medium)
    }
}

// Señal PCG simulada (luego se reemplaza por los datos del ESP32)
@Composable
private fun SenalSimulada(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "onda")
    val fase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart),
        label = "fase"
    )

    Canvas(modifier.background(Color(0xFF0B2A2F), RoundedCornerShape(12.dp))) {
        val path = Path()
        val puntos = 300
        for (i in 0..puntos) {
            val x = i / puntos.toFloat()
            val t = (((x * 3f - fase) % 1f) + 1f) % 1f
            val s1 = exp(-((t - 0.2f) / 0.025f).pow(2))
            val s2 = 0.35f * exp(-((t - 0.6f) / 0.05f).pow(2))
            val y = size.height * (0.5f - 0.35f * (s1 + s2))
            val px = x * size.width
            if (i == 0) path.moveTo(px, y) else path.lineTo(px, y)
        }
        drawPath(path, Color(0xFF3DDC84), style = Stroke(width = 4f))
    }
}