package com.example.chat_application.presentation.ChatApp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chat_application.models.Chat
import com.example.chat_application.models.User
import com.example.chat_application.presentation.Authentication.AuthenticationRoutes

@Composable
fun ChatGraph(
    chatViewModel: ChatViewModel,
    navController: NavHostController
){

    val ChatViewModel: ChatViewModel = viewModel()

    NavHost(navController = navController, startDestination = ChatRoutes.AllChats.route) {
        composable(ChatRoutes.AllChats.route){ AllChatsComposable(navController = navController, viewModel = chatViewModel) }
        composable(ChatRoutes.RequestedChats.route){ RequestedChatsComposable(navController = navController) }
        composable(ChatRoutes.ChatScreen.route) { ChatScreen(navController = navController, viewModel = chatViewModel) }
    }
}

sealed class ChatRoutes(val route: String, val title: String){
    object AllChats: ChatRoutes(route = "splash", title = "splash route")
    object RequestedChats: ChatRoutes(route = "login", title = "login route")
    object ChatScreen: ChatRoutes(route = "chat", title = "chat route")
    object Settings: ChatRoutes(route = "signup", title = "signup route")
}