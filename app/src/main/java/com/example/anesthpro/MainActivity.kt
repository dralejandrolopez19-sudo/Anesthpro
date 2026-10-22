package com.example.anesthpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.anesthpro.data.AppDatabase
import com.example.anesthpro.screens.AssessmentScreen
import com.example.anesthpro.screens.DashboardScreen
import com.example.anesthpro.screens.PatientFormScreen
import com.example.anesthpro.ui.theme.AnesthProTheme
import com.example.anesthpro.viewmodel.AnesthesiaViewModel
import com.example.anesthpro.viewmodel.AnesthesiaViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnesthProTheme {
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
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val viewModel: AnesthesiaViewModel = viewModel(
        factory = AnesthesiaViewModelFactory(db)
    )

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("new_patient") {
            PatientFormScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = "assessment/{patientId}",
            arguments = listOf(
                navArgument("patientId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getLong("patientId") ?: 0L
            AssessmentScreen(
                patientId = patientId,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
