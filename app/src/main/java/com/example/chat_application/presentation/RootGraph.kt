package com.example.chat_application.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.presentation.Authentication.AuthenticationRoutes
import com.example.chat_application.presentation.Authentication.authenticationGraph
import com.example.chat_application.presentation.ChatApp.MainChatScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Root_graph_routes.AuthGraph.route
    ) {
        authenticationGraph(navController = navController)
        composable(Root_graph_routes.MainScreenRoute.route){ MainChatScreen( navController) }

    }
}

sealed class Root_graph_routes(val route: String, val title: String){
    object AuthGraph: Root_graph_routes("auth_graph_route", "auth graph route")
    object MainScreenRoute: Root_graph_routes("main_screen_route", "main screen route")
}