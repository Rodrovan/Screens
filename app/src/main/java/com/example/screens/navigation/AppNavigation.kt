package com.example.screens.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview // Asegúrate de tener esta importación
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screens.screens.LoginScreen
import com.example.screens.screens.MainScreen

// Define tus rutas principales (si aún no lo has hecho fuera)
object AppDestinations {
    const val LOGIN_ROUTE = "login"
    const val MAIN_ROUTE = "main"
}

// Asumiendo que tienes tu LoginScreen y MainScreen definidos como en ejemplos anteriores
// import com.example.yourapp.ui.theme.YourAppTheme // Reemplaza con la ruta a tu tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourAppTheme { // Reemplaza YourAppTheme con el nombre real de tu tema
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN_ROUTE // 1. Inicia aquí
    ) {
        composable(AppDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onLoginClick = { username, password ->
                    // AQUÍ VA TU LÓGICA DE AUTENTICACIÓN
                    // Por ejemplo, verificar usuario y contraseña
                    val loginExitoso = true // Simulación: reemplazar con lógica real

                    if (loginExitoso) {
                        // 2. Navega a MainScreen y elimina Login de la pila
                        navController.navigate(AppDestinations.MAIN_ROUTE) {
                            popUpTo(AppDestinations.LOGIN_ROUTE) {
                                inclusive = true // Esto elimina LoginScreen de la pila de retroceso
                            }
                            launchSingleTop = true // Evita múltiples instancias de MainScreen
                        }
                    } else {
                        // Manejar login fallido (mostrar mensaje de error, etc.)
                    }
                }
            )
        }
        composable(AppDestinations.MAIN_ROUTE) {
            MainScreen() // MainScreen tiene su propio NavController para el bottom nav
        }
    }
}

// Preview para AppNavigation (opcional pero útil)
@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    YourAppTheme {
        AppNavigation()
    }
}

// No olvides definir YourAppTheme o usar el tema por defecto de Material
@Composable
fun YourAppTheme(content: @Composable () -> Unit) {
    MaterialTheme { // O tu tema personalizado si lo tienes
        content()
    }
}
