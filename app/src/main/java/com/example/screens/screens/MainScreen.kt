package com.example.screens.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import bottomNavigationItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainNavController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavigationItems.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                    screen.selectedIcon
                                } else {
                                    screen.unselectedIcon
                                },
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            mainNavController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = Screen.Publish.route, // Inicia en Home
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Feed.route) { FeedScreen() }
            composable(Screen.Publish.route) {
                PublishScreen(
                    onPublishClick = { title, description, category, documentContent, fileUri, facultad ->
                        // Lógica para manejar la publicación:
                        // 1. Esta lambda ahora recibe 'facultad' además de los otros datos.
                        // 2. Idealmente, llamar a un ViewModel aquí. El ViewModel debería:
                        //    a. Recibir todos estos datos (title, description, category, documentContent, fileUri, facultad).
                        //    b. Encargarse de la lógica de negocio (validaciones adicionales si son necesarias).
                        //    c. Preparar y enviar los datos Y EL ARCHIVO al backend.
                        //    d. Manejar la respuesta del backend.
                        // 3. Después de una interacción exitosa con el ViewModel (y por ende, el backend):
                        //    a. Mostrar un mensaje de éxito (Snackbar/Toast), usualmente disparado desde el ViewModel a través de un StateFlow/LiveData.
                        //    b. Limpiar los campos del formulario si es necesario (el ViewModel puede exponer un evento para esto).
                        //    c. Navegar a otra pantalla (ej. de vuelta a Home o a la pantalla de Feed).
                        //       mainNavController.navigate(Screen.Home.route) { // O la ruta que corresponda
                        //           popUpTo(Screen.Publish.route) { inclusive = true } // Sale de PublishScreen del backstack
                        //       }

                        // Ejemplo de lo que se recibe para depuración:
                        println("Publicando desde MainScreen: Título - $title, Facultad - ${facultad?.displayName}")
                        println("Descripción: $description, Categoría: $category")
                        println("Contenido del Documento (si lo hay):\n$documentContent")
                        println("URI del Archivo Adjunto (si lo hay): $fileUri")

                    },
                    onNavigateBack = {
                        // Navega hacia atrás en la pila de navegación.
                        // Si PublishScreen es el destino actual en la parte superior de la pila,
                        // popBackStack() lo quitará y mostrará la pantalla anterior.
                        if (mainNavController.previousBackStackEntry != null) {
                            mainNavController.popBackStack()
                        } else {
                            // Opcional: si no hay un backstack previo (ej. PublishScreen fue el primer destino)
                            // podrías navegar a un destino por defecto como la pantalla de inicio.
                            // mainNavController.navigate(Screen.Home.route) { // O la ruta que corresponda
                            //    popUpTo(mainNavController.graph.findStartDestination().id) { inclusive = true }
                            // }
                            println("Info: No hay pantalla anterior en el backstack para retroceder desde PublishScreen.")
                        }
                    }
                )
            }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    MainScreen()

}