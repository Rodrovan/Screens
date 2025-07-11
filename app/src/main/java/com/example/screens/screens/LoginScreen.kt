package com.example.screens.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.password
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.screens.viewmodels.LoginUiState
import com.example.screens.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(), // Inyecta el ViewModel
    onLoginSuccess: () -> Unit // Callback para cuando el login es exitoso
) {
    val uiState = loginViewModel.loginUiState
    val context = LocalContext.current // Para mostrar Toasts o mensajes

    // Observa el estado para acciones como navegar o mostrar mensajes globales
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {
                // Podrías mostrar un Toast de éxito aquí antes de navegar si quieres
                // Toast.makeText(context, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
                // Es importante resetear el estado si el usuario puede volver a esta pantalla
                // sin recrear el ViewModel (ej. si está en un NavHost con estado guardado)
                // loginViewModel.resetState() // Opcional, depende de tu flujo de navegación
            }
            is LoginUiState.Error -> {
                // Puedes mostrar un Toast o un Snackbar aquí si no manejas el error en el TextField
                // Toast.makeText(context, uiState.message, Toast.LENGTH_LONG).show()
            }
            else -> { /* No hacer nada para Idle o Loading en LaunchedEffect por ahora */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión", // Puedes usar stringResource aquí
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = loginViewModel.username,
            onValueChange = { loginViewModel.updateUsername(it) },
            label = { Text("Usuario/Correo") }, // Puedes usar stringResource aquí
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = uiState is LoginUiState.Error, // Marcar error en el campo
            singleLine = true
        )

        OutlinedTextField(
            value = loginViewModel.password,
            onValueChange = { loginViewModel.updatePassword(it) },
            label = { Text("Contraseña") }, // Puedes usar stringResource aquí
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(),
            // .padding(bottom = if (uiState is LoginUiState.Error) 8.dp else 16.dp), // Ajusta padding si hay error
            isError = uiState is LoginUiState.Error, // Marcar error en el campo
            singleLine = true
        )

        // Mostrar mensaje de error debajo de los campos si hay uno
        if (uiState is LoginUiState.Error) {
            Text(
                text = uiState.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        } else {
            // Espacio para mantener la consistencia del layout cuando no hay error
            Spacer(modifier = Modifier.height(28.dp)) // Ajusta esta altura según el tamaño del Text de error
        }


        Button(
            onClick = { loginViewModel.onLoginClick() },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoginUiState.Loading // Deshabilitar botón mientras carga
        ) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(24.dp), // Ajusta el tamaño según tu Text
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Entrar") // Puedes usar stringResource aquí
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview_Idle() {
    // Es buena práctica envolver tus previews con el tema de tu app
    // Ejemplo: YourAppTheme {
    val previewViewModel = LoginViewModel() // ViewModel para la preview
    LoginScreen(loginViewModel = previewViewModel, onLoginSuccess = {
        println("Preview: Login Exitoso")
    })
    // }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview_Loading() {
    val previewViewModel = LoginViewModel()
    // Simular estado de carga para la preview
    // (Necesitaríamos exponer un método para setear estado directamente o simular el click)
    // Para una preview simple, podemos instanciar un ViewModel y llamar al método que cambia el estado
    // pero no podemos simular el viewModelScope.launch directamente en una preview de forma sencilla.
    // Una alternativa es crear un estado específico en el ViewModel para previews o usar herramientas de testing.
    // Por ahora, mostraremos el estado inicial. Si quieres un preview del loading,
    // puedes temporalmente setear loginUiState a Loading en el ViewModel para visualizarlo.
    LoginScreen(loginViewModel = previewViewModel, onLoginSuccess = {})
    // previewViewModel.onLoginClick() // Esto no funcionará bien en preview para estados asíncronos
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview_Error() {
    val previewViewModel = LoginViewModel()
    // Para simular el error, necesitaríamos una forma de establecer este estado en el ViewModel
    // o modificar el ViewModel temporalmente para que inicie en estado de error.
    // Ejemplo de cómo podrías "forzar" un estado para la preview (no ideal para el ViewModel real):
    // previewViewModel.forceErrorState("Usuario o contraseña incorrectos.") // Método hipotético
    LoginScreen(loginViewModel = previewViewModel, onLoginSuccess = {})
}
