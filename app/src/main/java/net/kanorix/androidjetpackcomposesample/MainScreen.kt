package net.kanorix.androidjetpackcomposesample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.kanorix.androidjetpackcomposesample.ui.ListScreen
import net.kanorix.androidjetpackcomposesample.ui.MapScreen
import net.kanorix.androidjetpackcomposesample.ui.MapViewModel
import net.kanorix.androidjetpackcomposesample.ui.RequestScreen
import net.kanorix.androidjetpackcomposesample.ui.SearchDirectionScreen

enum class Screen(val title: String) {
    Map("Map Screen"), List("List Screen"), Request("Request Screen"), TabletMap("Tablet Map Screen"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    viewModel: MapViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Map.name
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(currentScreen.name) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    if (navController.previousBackStackEntry == null && currentScreen == Screen.Map) {
                        IconButton(onClick = { navController.navigate(Screen.List.name) }) {
                            Icon(imageVector = Icons.Filled.List, contentDescription = "list")
                        }
                    } else {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Filled.Place, contentDescription = "map")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    if (currentScreen == Screen.Request) {
                        IconButton(onClick = {
                            navController.navigate(Screen.TabletMap.name)
                        },
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "search")
                        }
                    } else {
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.Request.name)
                            },
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Icon(Icons.Filled.Send, contentDescription = "request")
                        }
                    }
                }
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = Screen.Map.name) {
                MapScreen(mapViewModel = viewModel)
            }
            composable(route = Screen.List.name) {
                ListScreen(mapViewModel = viewModel)
            }
            composable(route = Screen.Request.name) {
                RequestScreen()
            }
            composable(route = Screen.TabletMap.name) {
                SearchDirectionScreen()
            }
        }
    }
}