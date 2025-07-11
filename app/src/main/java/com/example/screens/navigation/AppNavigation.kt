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
            // Ya no pasas onLoginClick directamente con username y password.
            // LoginScreen ahora usa su propio ViewModel para manejar la lógica de login.
            // Solo necesitas proporcionar la acción a realizar CUANDO el login es exitoso.
            LoginScreen(
                // loginViewModel = viewModel(), // El ViewModel se obtiene por defecto así si no se pasa
                onLoginSuccess = {
                    // Navega a MainScreen y elimina Login de la pila
                    // Esta lógica ahora se ejecuta cuando el LoginViewModel indica un éxito.
                    navController.navigate(AppDestinations.MAIN_ROUTE) {
                        popUpTo(AppDestinations.LOGIN_ROUTE) {
                            inclusive = true // Esto elimina LoginScreen de la pila de retroceso
                        }
                        launchSingleTop = true // Evita múltiples instancias de MainScreen
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
