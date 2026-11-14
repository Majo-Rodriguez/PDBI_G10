package com.example.cardiocopy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

object Routes {
    const val SPLASH = "splash"
    const val MENU = "menu"
    const val VINCULAR = "vincular"
    const val NUEVO_REGISTRO = "nuevo_registro"
    const val GUIA = "guia"
    const val HISTORIAL = "historial"
    const val DETALLE = "detalle/{numero}"
    fun detalle(numero: Int) = "detalle/$numero"
    const val ENTREGAR = "entregar/{numero}"
    fun entregar(numero: Int) = "entregar/$numero"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()

    // Base de datos local (Room) y un "lanzador" para las operaciones de guardado
    val context = LocalContext.current
    val dao = remember { AppDatabase.obtener(context).registroDao() }
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onFinish = {
                navController.navigate(Routes.MENU) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.MENU) {
            MenuScreen(
                onNuevoRegistro = { navController.navigate(Routes.VINCULAR) },
                onHistorial = { navController.navigate(Routes.HISTORIAL) },
                onGuia = { navController.navigate(Routes.GUIA) }
            )
        }
        composable(Routes.VINCULAR) {
            BienvenidaScreen(
                onVincular = { /* TODO: escaneo BLE */ },
                onContinuar = { navController.navigate(Routes.NUEVO_REGISTRO) }
            )
        }
        composable(Routes.NUEVO_REGISTRO) {
            NuevoRegistroScreen(
                onBack = { navController.popBackStack() },
                onTerminar = {
                    // Guarda el registro en la base de datos y abre su detalle
                    scope.launch {
                        val id = dao.insertar(registroSimulado()).toInt()
                        navController.navigate(Routes.detalle(id)) {
                            popUpTo(Routes.MENU)
                        }
                    }
                }
            )
        }
        composable(Routes.GUIA) {
            GuiaScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.HISTORIAL) {
            // Lee todos los registros guardados; la lista se actualiza sola
            val registros by remember { dao.todos() }.collectAsState(initial = emptyList())
            HistorialScreen(
                registros = registros.map { it.aResumen() },
                onBack = { navController.popBackStack() },
                onAbrir = { numero -> navController.navigate(Routes.detalle(numero)) }
            )
        }
        composable(
            route = Routes.DETALLE,
            arguments = listOf(navArgument("numero") { type = NavType.IntType })
        ) { entry ->
            val numero = entry.arguments?.getInt("numero") ?: 1
            val registro by remember(numero) { dao.porId(numero) }.collectAsState(initial = null)

            registro?.let { r ->
                DetalleScreen(
                    numeroRegistro = r.id,
                    fecha = formatearFecha(r.fechaMillis),
                    hallazgos = r.hallazgos(),
                    onBack = { navController.popBackStack() },
                    // El botón de entregar solo aparece si el registro sigue pendiente
                    onEntregar = if (r.estado == EstadoEntrega.PENDIENTE.name) {
                        { navController.navigate(Routes.entregar(r.id)) }
                    } else null
                )
            }
        }
        composable(
            route = Routes.ENTREGAR,
            arguments = listOf(navArgument("numero") { type = NavType.IntType })
        ) { entry ->
            val numero = entry.arguments?.getInt("numero") ?: 1
            EntregarScreen(
                numeroRegistro = numero,
                onBack = { navController.popBackStack() },
                onEntregado = {
                    // Marca el registro como entregado en la base de datos
                    scope.launch { dao.cambiarEstado(numero, EstadoEntrega.ENTREGADO.name) }
                },
                onTerminar = { navController.popBackStack(Routes.MENU, inclusive = false) }
            )
        }
    }
}