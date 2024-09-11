package com.example.redchat.presentation.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.redchat.presentation.main.chat_screen.ChatScreen
import com.example.redchat.presentation.main.main_screen.MainScreen

val MAIN_GRAPH_ROUTE = "main_graph_route"

@Composable
fun MainNavGraph(
    navController: NavHostController
){
    val myNavController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()

    NavHost(
        navController = myNavController,
        startDestination = MainGraphRoutes.MainScreen.route,
        route = MAIN_GRAPH_ROUTE
    ){

        composable(MainGraphRoutes.MainScreen.route){
            MainScreen(navController = myNavController, viewModel = mainViewModel)
        }
        composable(MainGraphRoutes.ChatScreen.route){
            ChatScreen(navController = myNavController, viewModel = mainViewModel, friend = mainViewModel.selectedFriend)
        }

    }

}

sealed class MainGraphRoutes(
    val route: String,
    val title: String
){
    object MainScreen: MainGraphRoutes(route = "main_screen_route", title = "Login Screen Route")
    object ChatScreen: MainGraphRoutes(route = "chat_screen_route", title = "Chat Screen Route")
}