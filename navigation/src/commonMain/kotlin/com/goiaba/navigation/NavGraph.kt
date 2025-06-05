package com.goiaba.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.goiaba.details.DetailsScreen
//import com.goiaba.details.DetailsScreen
import com.goiaba.feature.HomeGraphScreen
import com.goiaba.shared.navigation.Screen

@Composable
fun SetupNavGraph(startDestination: Screen = Screen.HomeGraph) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToDetails = { productId ->
                    navController.navigate(Screen.Details(id = productId))
                },
            )
        }
        composable<Screen.Details> {
            DetailsScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.PaymentCompleted> {
        }
    }
}