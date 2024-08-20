package com.example.chat_application.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.presentation.Authentication.authenticationGraph
import com.example.chat_application.presentation.ChatApp.MainChatScreen

@Composable
fun RootGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Root_graph_routes.AuthGraph.route) {
        authenticationGraph(navController = navController)
        composable(Root_graph_routes.MainScreenRoute.route){ MainChatScreen(navController = navController) }

    }
}

sealed class Root_graph_routes(val route: String, val title: String){
    object AuthGraph: Root_graph_routes("auth_graph_route", "auth graph route")
    object MainScreenRoute: Root_graph_routes("main_screen_route", "auth graph route")
}