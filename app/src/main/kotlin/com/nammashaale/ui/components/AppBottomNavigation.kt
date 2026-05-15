package com.nammashaale.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nammashaale.navigation.Screen

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun AppBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem(Screen.Dashboard.route, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem(Screen.AssetList.route, "Assets", Icons.Filled.Inventory, Icons.Outlined.Inventory2),
        BottomNavItem(Screen.Audit.route, "Audit", Icons.Filled.Assignment, Icons.Outlined.Assignment),
        BottomNavItem(Screen.Reports.route, "Reports", Icons.Filled.Analytics, Icons.Outlined.Analytics),
        BottomNavItem(Screen.AiAssistant.route, "AI", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
        BottomNavItem(Screen.Profile.route, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
