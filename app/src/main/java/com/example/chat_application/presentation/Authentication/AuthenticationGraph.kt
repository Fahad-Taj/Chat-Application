package com.example.chat_application.presentation.Authentication

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.chat_application.presentation.Authentication.login.LoginComposable
import com.example.chat_application.presentation.Authentication.login.LoginScreenState
import com.example.chat_application.presentation.Authentication.login.LoginViewmodel
import com.example.chat_application.presentation.Authentication.signup.SignUpComposable
import com.example.chat_application.presentation.Authentication.signup.SignupViewmodel
import com.example.chat_application.presentation.Root_graph_routes

fun NavGraphBuilder.authenticationGraph(navController: NavHostController) {
    val loginViewmodel = LoginViewmodel()
    val signupViewmodel = SignupViewmodel()

    navigation(
        startDestination = AuthenticationRoutes.Login.route,
        route = Root_graph_routes.AuthGraph.route
    ) {
        composable(AuthenticationRoutes.Login.route) {
            LoginComposable(
                navController = navController,
                viewModel = loginViewmodel,
                state = LoginScreenState()
            )
        }
        composable(AuthenticationRoutes.SignUp.route) {
            SignUpComposable(navController = navController)
        }
    }
}

sealed class AuthenticationRoutes(val route: String, val title: String) {
    object Login : AuthenticationRoutes(route = "login", title = "login route")
    object SignUp : AuthenticationRoutes(route = "signup", title = "signup route")
}