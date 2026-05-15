package com.nammashaale.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashaale.NammaShaaleApplication
import com.nammashaale.ui.components.AppBottomNavigation
import com.nammashaale.ui.screens.*
import com.nammashaale.viewmodel.AppViewModelFactory

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val application = context.applicationContext as NammaShaaleApplication
    val viewModelFactory = AppViewModelFactory(application.repository)

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        Screen.Dashboard.route,
        Screen.AssetList.route,
        Screen.Audit.route,
        Screen.Reports.route,
        Screen.AiAssistant.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                AppBottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                val sessionManager = remember { com.nammashaale.utils.SessionManager(context) }
                SplashScreen(onNavigateToLogin = {
                    val destination = if (sessionManager.isLoggedIn()) Screen.Dashboard.route else Screen.Login.route
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Login.route) {
                LoginScreen(onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel = viewModel(factory = viewModelFactory))
            }
            composable(Screen.AssetList.route) {
                AssetListScreen(
                    onNavigateToAddAsset = { navController.navigate(Screen.AddAsset.route) },
                    onNavigateToAssetDetails = { assetId ->
                        navController.navigate(Screen.AssetDetails.createRoute(assetId))
                    },
                    viewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable(Screen.AddAsset.route) {
                AddAssetScreen(
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable(
                route = Screen.AssetDetails.route,
                arguments = listOf(navArgument("assetId") { type = NavType.IntType })
            ) { backStackEntry ->
                val assetId = backStackEntry.arguments?.getInt("assetId") ?: 0
                AssetDetailsScreen(
                    assetId = assetId,
                    onNavigateBack = { navController.popBackStack() },
                    assetViewModel = viewModel(factory = viewModelFactory),
                    repairViewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable(Screen.Audit.route) {
                AuditScreen(
                    assetViewModel = viewModel(factory = viewModelFactory),
                    auditViewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable(Screen.Repair.route) {
                RepairScreen(
                    repairViewModel = viewModel(factory = viewModelFactory),
                    assetViewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable(Screen.Reports.route) {
                ReportsScreen(viewModel = viewModel(factory = viewModelFactory))
            }
            composable(Screen.AiAssistant.route) {
                AiAssistantScreen(
                    assetViewModel = viewModel(factory = viewModelFactory),
                    auditViewModel = viewModel(factory = viewModelFactory),
                    repairViewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }
        }
    }
}
