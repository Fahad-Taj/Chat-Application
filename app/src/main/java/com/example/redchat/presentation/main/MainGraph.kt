package com.example.redchat.presentation.main

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

val MAIN_GRAPH_ROUTE = "main_graph_route"


fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController
){
    navigation(
        startDestination = MainGraphRoutes.MainScreen.route,
        route = MAIN_GRAPH_ROUTE
    ){
        composable(MainGraphRoutes.MainScreen.route){
            MainScreen(navController = navController, viewModel = viewModel())
        }
    }


}

sealed class MainGraphRoutes(
    val route: String,
    val title: String
){
    object MainScreen: MainGraphRoutes(route = "main_screen_route", title = "Login Screen Route")
    object SignupScreen: MainGraphRoutes(route = "signup_screen_route", title = "Signup Screen Route")
}