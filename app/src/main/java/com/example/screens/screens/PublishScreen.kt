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
import androidx.compose.runtime.* // Importante para by, mutableStateOf, etc.
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.screens.model.Facultad // Importa tu enum de Facultad
import com.example.screens.state.rememberPublishScreenState
import kotlin.text.isNotBlank

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishScreen(
    // Actualiza la lambda para incluir la URI del archivo y la facultad
    onPublishClick: (
        title: String,
        description: String,
        category: String,
        documentContent: String,
        fileUri: Uri?,
        facultad: Facultad? // Añadido
    ) -> Unit,
    onNavigateBack: (() -> Unit)? = null
) {
    val state = rememberPublishScreenState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Estado para el dropdown de facultad
    var expandedFacultadDropdown by remember { mutableStateOf(false) }
    val facultadesList = remember { Facultad.values().toList() }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            state.attachedFileUri = it
            val cursor = context.contentResolver.query(it, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val nameIndex = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        state.attachedFileName = c.getString(nameIndex)
                    }
                }
            }
            if (state.attachedFileName == null) {
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
                    // Considera añadir validación para state.selectedFacultad si es requerido
                    if (state.title.isNotBlank() &&
                        (state.documentContent.isNotBlank() || state.attachedFileUri != null) &&
                        state.category.isNotBlank() /* && state.selectedFacultad != null (si es obligatorio) */) {
                        onPublishClick(
                            state.title,
                            state.description,
                            state.category,
                            state.documentContent,
                            state.attachedFileUri,
                            state.selectedFacultad // Pasar la facultad seleccionada
                        )
                    } else {
                        // Manejar campos requeridos
                        // Podrías mostrar un Snackbar o un Toast aquí
                        println("Error: Faltan campos requeridos o la facultad no está seleccionada.")
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

            // Selector de Facultad
            ExposedDropdownMenuBox(
                expanded = expandedFacultadDropdown,
                onExpandedChange = { expandedFacultadDropdown = !expandedFacultadDropdown },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.selectedFacultad?.displayName ?: "Selecciona una Facultad",
                    onValueChange = { /* No editable directamente */ },
                    label = { Text("Facultad (Opcional)") }, // Hazlo opcional o no según tus reglas
                    readOnly = true,
                    leadingIcon = { Icon(Icons.Filled.School, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFacultadDropdown) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedFacultadDropdown,
                    onDismissRequest = { expandedFacultadDropdown = false }
                ) {
                    facultadesList.forEach { facultad ->
                        DropdownMenuItem(
                            text = { Text(facultad.displayName) },
                            onClick = {
                                state.selectedFacultad = facultad
                                expandedFacultadDropdown = false
                            }
                        )
                    }
                }
            }


            OutlinedTextField(
                value = state.documentContent,
                onValueChange = { state.documentContent = it },
                label = { Text("Contenido Escrito (Opcional si adjuntas archivo)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 150.dp),
                leadingIcon = { Icon(Icons.Filled.Article, null) },
                keyboardOptions = KeyboardOptions.Default.copy(KeyboardCapitalization.Sentences),
                maxLines = 15
            )

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
                    filePickerLauncher.launch("*/*")
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
@Preview(showBackground = true, heightDp = 900) // Aumenté el height para ver más
@Composable
fun PublishScreenWithAttachmentAndFacultadPreview() {
    MaterialTheme {
        PublishScreen(
            onPublishClick = { title, description, category, documentContent, fileUri, facultad ->
                println("Preview Publicar: Título - $title, Desc - $description, Cat - $category, Facultad - ${facultad?.displayName}")
                println("Contenido Doc:\n$documentContent")
                println("Archivo Adjunto URI: $fileUri")
            },
            onNavigateBack = { println("Preview: Navegar atrás") }
        )
    }
}

