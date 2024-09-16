package com.example.chat_application.presentation.ChatApp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chat_application.presentation.ChatApp.AllChat.AllChatsComposable
import com.example.chat_application.presentation.ChatApp.AllChat.ChatViewModel
import com.example.chat_application.presentation.ChatApp.RequestedChat.RequestedChatsComposable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatGraph(
    chatViewModel: ChatViewModel,
    navController: NavHostController
){

    NavHost(navController = navController, startDestination = ChatRoutes.AllChats.route) {
        composable(ChatRoutes.AllChats.route){ AllChatsComposable(navController = navController, viewModel = chatViewModel) }
        composable(ChatRoutes.RequestedChats.route){ RequestedChatsComposable(navController = navController, rootViewModel = chatViewModel) }
        composable(ChatRoutes.ChatScreen.route) { ChatScreen(navController = navController, viewModel = chatViewModel) }
    }
}

sealed class ChatRoutes(val route: String, val title: String){
    object AllChats: ChatRoutes(route = "splash", title = "splash route")
    object RequestedChats: ChatRoutes(route = "login", title = "login route")
    object ChatScreen: ChatRoutes(route = "chat", title = "chat route")
    object Settings: ChatRoutes(route = "signup", title = "signup route")
}