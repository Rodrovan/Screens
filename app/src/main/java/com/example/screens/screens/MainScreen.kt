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
            startDestination = Screen.Home.route, // Inicia en Home
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Feed.route) { FeedScreen() }
            composable(Screen.Publish.route) {
                PublishScreen(
                    onPublishClick = { title, description, category, documentContent, fileUri ->
                        // Lógica para manejar la publicación:
                        // 1. Llamar a un ViewModel para guardar/enviar los datos Y EL ARCHIVO.
                        // 2. Mostrar un mensaje de éxito (Snackbar).
                        // 3. Limpiar los campos o navegar a otra pantalla.
                        println("Publicando: Título - $title, Descripción - $description, Categoría - $category")
                        println("Contenido del Documento:\n$documentContent")
                        println("URI del Archivo Adjunto: $fileUri") // Ahora puedes usar fileUri

                        // Ejemplo: Navegar de vuelta a Home después de publicar
                        // mainNavController.navigate(Screen.Home.route) {
                        //     popUpTo(Screen.Publish.route) { inclusive = true }
                        // }
                    },
                onNavigateBack = {
                    println("Preview: Navegar hacia atrás")
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