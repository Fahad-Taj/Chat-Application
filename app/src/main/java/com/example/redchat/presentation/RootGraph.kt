package com.example.redchat.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.redchat.presentation.authentication.AUTH_GRAPH_ROUTE
import com.example.redchat.presentation.authentication.authNavGraph
import com.example.redchat.presentation.main.MainNavGraph

val ROOT_GRAPH_ROUTE = "root_graph_route"

@Composable
fun RootGraph(navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = AUTH_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE
    ) {
        authNavGraph(navController = navController)
        composable("main_nav_graph"){
            MainNavGraph(navController = navController)
        }

    }

}

