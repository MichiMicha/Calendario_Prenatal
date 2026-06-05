package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.example.myapplication.ui.screens.SettingsScreen
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

    val isConfigured by userViewModel.isUserConfigured.collectAsState()

    if (isConfigured == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF7F2EE)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFE2725B))
        }
    } else {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                if (currentRoute == "home" || currentRoute == "calendar" || currentRoute == "settings") {
                    BottomNavigationBar(navController, currentRoute)
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = if (isConfigured == true) "home" else "welcome",
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
                    CalendarScreen(userViewModel, onAddEntryClick = {})
                }
                composable("settings") {
                    SettingsScreen(userViewModel)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    val terracotta = Color(0xFFE2725B)

    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") { popUpTo(navController.graph.startDestinationId) }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = terracotta,
                selectedTextColor = terracotta,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFF7F2EE)
            )
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
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = terracotta,
                selectedTextColor = terracotta,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFF7F2EE)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
            label = { Text("Ajustes") },
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") {
                    navController.navigate("settings") { popUpTo(navController.graph.startDestinationId) }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = terracotta,
                selectedTextColor = terracotta,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color(0xFFF7F2EE)

            )
        )
    }
}