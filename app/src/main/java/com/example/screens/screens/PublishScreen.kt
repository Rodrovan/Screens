package com.example.screens.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.screens.state.rememberPublishScreenState // Asegúrate que esta clase tiene los nuevos estados
import kotlin.text.isNotBlank

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishScreen(
    // Actualiza la lambda para incluir la URI del archivo
    onPublishClick: (title: String, description: String, category: String, documentContent: String, fileUri: Uri?) -> Unit,
    onNavigateBack: (() -> Unit)? = null
) {
    val state = rememberPublishScreenState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // ActivityResultLauncher para seleccionar un archivo
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent() // Puedes usar OpenDocument() para más control
    ) { uri: Uri? ->
        uri?.let {
            state.attachedFileUri = it
            // Intenta obtener el nombre del archivo (puede requerir permisos o ser más complejo)
            // Esta es una forma simple, puede no funcionar para todos los URIs/proveedores
            val cursor = context.contentResolver.query(it, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val nameIndex = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        state.attachedFileName = c.getString(nameIndex)
                    }
                }
            }
            if (state.attachedFileName == null) { // Fallback si no se pudo obtener el nombre
                state.attachedFileName = it.lastPathSegment ?: "Archivo adjunto"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Publicación") },
                navigationIcon = {
                    if (onNavigateBack != null) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, "Volver")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (state.title.isNotBlank() && (state.documentContent.isNotBlank() || state.attachedFileUri != null) && state.category.isNotBlank()) {
                        onPublishClick(
                            state.title,
                            state.description,
                            state.category,
                            state.documentContent,
                            state.attachedFileUri
                        )
                    } else {
                        // Manejar campos requeridos (o al menos uno entre contenido de texto o archivo)
                    }
                },
                icon = { Icon(Icons.Filled.NewLabel, "Publicar") },
                text = { Text("Publicar") }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { state.title = it },
                label = { Text("Título del Material") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Title, null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(KeyboardCapitalization.Sentences)
            )

            // Contenido de texto (sigue siendo importante)
            OutlinedTextField(
                value = state.documentContent,
                onValueChange = { state.documentContent = it },
                label = { Text("Contenido Escrito (Opcional si adjuntas archivo)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 150.dp), // Reducido un poco si el foco es el adjunto
                leadingIcon = { Icon(Icons.Filled.Article, null) },
                keyboardOptions = KeyboardOptions.Default.copy(KeyboardCapitalization.Sentences),
                maxLines = 15
            )

            // Sección para Adjuntar Archivo
            Spacer(modifier = Modifier.height(8.dp))
            Text("Adjuntar Archivo (Opcional)", style = MaterialTheme.typography.titleSmall)

            if (state.attachedFileName != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.Filled.AttachFile, contentDescription = "Archivo adjunto")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = state.attachedFileName ?: "Error al obtener nombre",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        state.attachedFileUri = null
                        state.attachedFileName = null
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Quitar archivo")
                    }
                }
            }

            Button(
                onClick = {
                    filePickerLauncher.launch("*/*") // Lanza el selector de archivos, "*/*" para cualquier tipo
                    // Puedes ser más específico: "image/*", "application/pdf", etc.
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.UploadFile, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(if (state.attachedFileUri == null) "Seleccionar Archivo" else "Cambiar Archivo")
            }
            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = state.description,
                onValueChange = { state.description = it },
                label = { Text("Descripción breve / Resumen (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.ShortText, null) },
                maxLines = 5,
                keyboardOptions = KeyboardOptions.Default.copy(KeyboardCapitalization.Sentences)
            )

            OutlinedTextField(
                value = state.category,
                onValueChange = { state.category = it },
                label = { Text("Categoría (Ej: Apuntes, Parcial)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Filled.Category, null) },
                singleLine = true
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PublishScreenWithAttachmentPreview() {
    MaterialTheme {
        PublishScreen(
            onPublishClick = { title, description, category, documentContent, fileUri ->
                println("Preview Publicar: Título - $title, Desc - $description, Cat - $category")
                println("Contenido Doc:\n$documentContent")
                println("Archivo Adjunto URI: $fileUri")
            },
            onNavigateBack = { println("Preview: Navegar atrás") }
        )
    }
}

