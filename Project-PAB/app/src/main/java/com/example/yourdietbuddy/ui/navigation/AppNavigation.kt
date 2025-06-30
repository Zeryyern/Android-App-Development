package com.example.yourdietbuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yourdietbuddy.ui.screen.DashboardScreen
import com.example.yourdietbuddy.ui.screen.DetailScreen
import com.example.yourdietbuddy.ui.screen.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen() }
        composable("home") { HomeScreen(navController) }
        composable("detail/{itemName}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("itemName") ?: "No Name"
            DetailScreen(name)
        }
    }
}