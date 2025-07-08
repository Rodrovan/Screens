import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Feed
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Feed : Screen("feed", "Feed", Icons.Filled.Feed, Icons.Outlined.Feed)
    object Publish : Screen("publish", "Publicar", Icons.Filled.AddCircle, Icons.Outlined.AddCircle)
    object Profile : Screen("profile", "Perfil", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
}

val bottomNavigationItems = listOf(
    Screen.Feed, // Primero a la izquierda
    Screen.Home, // En el medio (segundo)
    Screen.Publish, // Tercero
    Screen.Profile // Último a la derecha
)
