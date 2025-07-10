package com.example.screens.state

import android.net.Uri
import androidx.compose.runtime.*

// Dentro o fuera de tu Composable PublishScreen, según prefieras
// Si usas un ViewModel, estos estarían en el ViewModel.
@Composable
fun rememberPublishScreenState(): PublishScreenState {
    return remember { PublishScreenState() }
}

class PublishScreenState {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var category by mutableStateOf("")
    var documentContent by mutableStateOf("")
    var attachedFileUri by mutableStateOf<Uri?>(null) // Para la URI del archivo adjunto
    var attachedFileName by mutableStateOf<String?>(null) // Para mostrar el nombre del archivo
}

