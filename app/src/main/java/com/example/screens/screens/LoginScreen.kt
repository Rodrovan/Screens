package com.example.screens.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión", // Puedes usar stringResource aquí
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.Companion.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") }, // Puedes usar stringResource aquí
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") }, // Puedes usar stringResource aquí
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = { onLoginClick(username, password) },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Entrar") // Puedes usar stringResource aquí
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Es buena práctica envolver tus previews con el tema de tu app
    // Ejemplo: YourAppTheme {
    LoginScreen(onLoginClick = { user, pass ->
        // Lógica de ejemplo para el preview
        println("Preview Login: Usuario - $user, Contraseña - $pass")
    })
    // }
}