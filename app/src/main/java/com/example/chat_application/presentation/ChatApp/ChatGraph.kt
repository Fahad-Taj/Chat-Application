package com.example.chat_application.presentation.ChatApp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chat_application.presentation.Authentication.AuthenticationRoutes

@Composable
fun ChatGraph(navController: NavHostController){

    val allChatViewModel = AllChatViewModel()

    NavHost(navController = navController, startDestination = ChatRoutes.AllChats.route) {
        composable(ChatRoutes.AllChats.route){ AllChatsComposable(navController = navController, viewModel = allChatViewModel) }
        composable(ChatRoutes.RequestedChats.route){ RequestedChatsComposable(navController = navController) }
    }
}

sealed class ChatRoutes(val route: String, val title: String){
    object AllChats: ChatRoutes(route = "splash", title = "splash route")
    object RequestedChats: ChatRoutes(route = "login", title = "login route")
    object Settings: ChatRoutes(route = "signup", title = "signup route")
}