package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.CalendarScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.PregnancyDetailsScreen
import com.example.myapplication.ui.screens.WelcomeScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            // Mostramos la barra de navegación en ambas pantallas principales
            if (currentRoute == "home" || currentRoute == "calendar") {
                BottomNavigationBar(navController, currentRoute)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("welcome") {
                WelcomeScreen(userViewModel, onNavigateToHome = { navController.navigate("pregnancy_details") })
            }
            composable("pregnancy_details") {
                PregnancyDetailsScreen(userViewModel, onNavigateToHome = {
                    navController.navigate("home") { popUpTo("welcome") { inclusive = true } }
                })
            }
            composable("home") {
                HomeScreen(userViewModel)
            }

            composable("calendar") {
                CalendarScreen(userViewModel, onAddEntryClick = {
                    navController.navigate("add_entry")
                })
            }
            
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") { popUpTo(navController.graph.startDestinationId) }
                }
            },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF9A6B52), selectedTextColor = Color(0xFF9A6B52))
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendario") },
            label = { Text("Calendario") },
            selected = currentRoute == "calendar",
            onClick = {
                if (currentRoute != "calendar") {
                    navController.navigate("calendar") { popUpTo(navController.graph.startDestinationId) }
                }
            },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF9A6B52), selectedTextColor = Color(0xFF9A6B52))
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
            label = { Text("Ajustes") },
            selected = currentRoute == "settings",
            onClick = { /* Futuro */ }
        )
    }
}