package com.example.screens.state // O donde esté tu clase de estado

import android.net.Uri
import androidx.compose.runtime.*
import com.example.screens.model.Facultad // Importa tu nuevo enum

class PublishScreenState {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var category by mutableStateOf("")
    var documentContent by mutableStateOf("")
    var attachedFileUri by mutableStateOf<Uri?>(null)
    var attachedFileName by mutableStateOf<String?>(null)
    var selectedFacultad by mutableStateOf<Facultad?>(null) // Nuevo estado para la facultad
}

@Composable
fun rememberPublishScreenState(): PublishScreenState {
    return remember { PublishScreenState() }
}
