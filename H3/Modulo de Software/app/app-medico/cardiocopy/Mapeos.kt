package com.example.cardiocopy

import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

// Convierte los milisegundos guardados en la base de datos a texto legible.
fun formatearFecha(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy · HH:mm", Locale.forLanguageTag("es-PE")).format(Date(millis))

// Convierte un registro de la base de datos al resumen que muestra el Historial.
fun RegistroEntity.aResumen(): RegistroResumen = RegistroResumen(
    numero = id,
    fecha = formatearFecha(fechaMillis),
    soplos = cantidadSoplos,
    estado = runCatching { EstadoEntrega.valueOf(estado) }.getOrDefault(EstadoEntrega.PENDIENTE)
)

// Convierte un registro de la base de datos a la lista de focos que muestra el Detalle.
fun RegistroEntity.hallazgos(): List<HallazgoFoco> = listOf(
    HallazgoFoco(1, "Foco Aórtico", Color(0xFFF9C6D3), soplo1),
    HallazgoFoco(2, "Foco Pulmonar", Color(0xFFD5F0A8), soplo2),
    HallazgoFoco(3, "Foco Tricúspide", Color(0xFFFCEB5C), soplo3),
    HallazgoFoco(4, "Foco Mitral / Apexiano", Color(0xFFF9B97C), soplo4)
)

// SIMULACIÓN: como todavía no hay análisis real de las señales, el resultado
// se elige al azar. Más adelante aquí irá el resultado del análisis verdadero.
fun registroSimulado(): RegistroEntity {
    val ahora = System.currentTimeMillis()
    return when (Random.nextInt(3)) {
        0 -> RegistroEntity(fechaMillis = ahora) // normal
        1 -> RegistroEntity(
            fechaMillis = ahora,
            soplo3 = "Soplo sistólico en la válvula tricúspide"
        )
        else -> RegistroEntity(
            fechaMillis = ahora,
            soplo1 = "Soplo sistólico en la válvula aórtica",
            soplo4 = "Soplo diastólico en la válvula mitral"
        )
    }
}