package com.example.screens.viewmodels // o el paquete que prefieras para ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Define los estados posibles de la UI de inicio de sesión
sealed interface LoginUiState {
    object Idle : LoginUiState // Estado inicial o después de un intento fallido y reseteado
    object Loading : LoginUiState // Cargando, mientras se procesa el login
    object Success : LoginUiState // Login exitoso
    data class Error(val message: String) : LoginUiState // Error durante el login
}

class LoginViewModel : ViewModel() {

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var loginUiState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    fun updateUsername(newUsername: String) {
        username = newUsername
        // Resetea el estado de error si el usuario empieza a escribir de nuevo
        if (loginUiState is LoginUiState.Error) {
            loginUiState = LoginUiState.Idle
        }
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        if (loginUiState is LoginUiState.Error) {
            loginUiState = LoginUiState.Idle
        }
    }

    fun onLoginClick() {
        // Validación básica (puedes añadir más)
        if (username.isBlank() || password.isBlank()) {
            loginUiState = LoginUiState.Error("Usuario y contraseña no pueden estar vacíos.")
            return
        }

        loginUiState = LoginUiState.Loading
        viewModelScope.launch {
            // --- SIMULACIÓN DE LLAMADA AL BACKEND ---
            // Aquí iría tu lógica real para llamar a tu repositorio/servicio de autenticación
            // Ejemplo:
            // try {
            //     val result = authRepository.login(username, password)
            //     if (result.isSuccess) {
            //         loginUiState = LoginUiState.Success
            //     } else {
            //         loginUiState = LoginUiState.Error(result.errorMessage ?: "Error desconocido")
            //     }
            // } catch (e: Exception) {
            //     loginUiState = LoginUiState.Error("Error de conexión: ${e.message}")
            // }

            // Simulación simple para el ejemplo:
            kotlinx.coroutines.delay(2000) // Simular delay de red
            if (username == "test" && password == "password") {
                loginUiState = LoginUiState.Success
            } else {
                loginUiState = LoginUiState.Error("Credenciales incorrectas.")
            }
            // --- FIN DE SIMULACIÓN ---
        }
    }

    // Opcional: Función para resetear el estado si es necesario desde fuera
    fun resetState() {
        loginUiState = LoginUiState.Idle
    }
}

