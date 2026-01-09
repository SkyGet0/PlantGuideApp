package com.example.plantguideapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.plantguideapp.ui.home.HomeScreen
import com.example.plantguideapp.ui.plants.PlantListScreen
import com.example.plantguideapp.ui.plants.PlantDetailScreen
import com.example.plantguideapp.ui.plants.FavoritePlantsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object PlantList : Screen("plant_list/{category}") {
        fun createRoute(category: String) = "plant_list/$category"
    }
    object PlantDetail : Screen("plant_detail/{id}") {
        fun createRoute(id: Int) = "plant_detail/$id"
    }
    object Favorites : Screen("favorites")
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(Screen.PlantList.createRoute(category))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        composable(
            route = Screen.PlantList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            PlantListScreen(
                category = category,
                onPlantClick = { plantId ->
                    navController.navigate(Screen.PlantDetail.createRoute(plantId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.PlantDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            PlantDetailScreen(
                plantId = id,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritePlantsScreen(
                onPlantClick = { id ->
                    navController.navigate(Screen.PlantDetail.createRoute(id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
