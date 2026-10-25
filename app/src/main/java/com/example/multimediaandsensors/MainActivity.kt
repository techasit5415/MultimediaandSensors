package com.example.multimediaandsensors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.multimediaandsensors.ui.camera.CameraScreen
import com.example.multimediaandsensors.ui.home.HomeScreen
import com.example.multimediaandsensors.ui.music.MusicScreen
import com.example.multimediaandsensors.ui.sensor.StepCounterScreen
import com.example.multimediaandsensors.ui.theme.MultimediaAndSensorsTheme
import com.example.multimediaandsensors.ui.video.VideoScreen

const val ROUTE_HOME = "home"
const val ROUTE_CAMERA = "camera"
const val ROUTE_MUSIC = "music"
const val ROUTE_VIDEO = "video"
const val ROUTE_SENSOR = "sensor"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultimediaAndSensorsTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = listOf(
        NavigationItem("Home", ROUTE_HOME, Icons.Default.Home),
        NavigationItem("Camera", ROUTE_CAMERA, Icons.Default.CameraAlt),
        NavigationItem("Music", ROUTE_MUSIC, Icons.Default.MusicNote),
        NavigationItem("Video", ROUTE_VIDEO, Icons.Default.VideoLibrary),
        NavigationItem("Sensor", ROUTE_SENSOR, Icons.AutoMirrored.Filled.DirectionsWalk)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ROUTE_HOME) {
                HomeScreen(
                    onCameraClick = { navController.navigate(ROUTE_CAMERA) },
                    onMusicClick = { navController.navigate(ROUTE_MUSIC) },
                    onVideoClick = { navController.navigate(ROUTE_VIDEO) },
                    onSensorClick = { navController.navigate(ROUTE_SENSOR) }
                )
            }
            composable(ROUTE_CAMERA) {
                CameraScreen(onBack = { navController.popBackStack() })
            }
            composable(ROUTE_MUSIC) {
                MusicScreen(onBack = { navController.popBackStack() })
            }
            composable(ROUTE_VIDEO) {
                VideoScreen(onBack = { navController.popBackStack() })
            }
            composable(ROUTE_SENSOR) {
                StepCounterScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
