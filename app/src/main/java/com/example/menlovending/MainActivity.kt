package com.example.menlovending

import LoadingScreen
import PermissionCheckScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.menlovending.ui.theme.MenloVendingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenloVendingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetupNavGraph(navController, this)
                }
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController, context: Context) {
    NavHost(
        navController = navController,
        startDestination = "loading"
    ) {
        composable("loading") {
            LoadingScreen(context, navController)
        }
        composable("permission") {
            PermissionCheckScreen(context, navController)
        }
    }
}