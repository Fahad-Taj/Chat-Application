package com.example.redchat.presentation.authentication

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.redchat.presentation.authentication.login.LoginScreen
import com.example.redchat.presentation.authentication.login.LoginViewmodel
import com.example.redchat.presentation.authentication.signup.SignupScreen
import com.example.redchat.presentation.authentication.uploadimage.UploadImageScreen

val AUTH_GRAPH_ROUTE = "auth_graph_route"


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
){
    navigation(
        startDestination = AuthGraphRoutes.LoginScreen.route,
        route = AUTH_GRAPH_ROUTE
    ){
        composable(AuthGraphRoutes.LoginScreen.route){
            LoginScreen(navController = navController, viewModel = viewModel())
        }
        composable(AuthGraphRoutes.SignupScreen.route){
            SignupScreen(navController = navController, viewModel = viewModel())
        }
        composable(AuthGraphRoutes.UploadImageScreen.route){
            UploadImageScreen(navController = navController, viewModel = viewModel())
        }
    }


}

sealed class AuthGraphRoutes(
    val route: String,
    val title: String
){
    object LoginScreen: AuthGraphRoutes(route = "login_screen_route", title = "Login Screen Route")
    object SignupScreen: AuthGraphRoutes(route = "signup_screen_route", title = "Signup Screen Route")
    object UploadImageScreen: AuthGraphRoutes(route = "upload_image_screen_route", title = "Upload Image Screen Route")
}