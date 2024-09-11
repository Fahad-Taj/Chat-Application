package com.example.redchat.presentation.authentication.login

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.redchat.api.access_token
import com.example.redchat.presentation.authentication.AuthGraphRoutes
import com.example.redchat.presentation.main.MainGraphRoutes

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewmodel
){

    val context = LocalContext.current

    // Main Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Contains the username and password input fields
        LoginInputFields(viewModel = viewModel)
        Spacer(modifier = Modifier.height(20.dp))

        // Button to invoke login function
        LoginButton(viewModel = viewModel, context = context, navController = navController)
        Spacer(modifier = Modifier.height(50.dp))

        // Signup Text
        SignupText(navController = navController, function = {
            navController.navigate(AuthGraphRoutes.SignupScreen.route)
        })

    }
}

@Composable
fun LoginInputFields(
    viewModel: LoginViewmodel
){
    // Username field
    OutlinedTextField(
        value = viewModel.username.value,
        onValueChange = {
            viewModel.username.value = it
        },
        label = { Text(text = "Username") }
    )
    Spacer(modifier = Modifier.height(10.dp))

    // Password field
    OutlinedTextField(
        value = viewModel.password.value,
        onValueChange = {
            viewModel.password.value = it
        },
        label = { Text(text = "Password") }
    )

}

@Composable
fun LoginButton(
    viewModel: LoginViewmodel,
    context: Context,
    navController: NavHostController
){

    val response by viewModel.response.collectAsState()

    LaunchedEffect(key1 = response) {
        response?.let {
            if(access_token != null){
                navController.navigate("main_nav_graph")
            }

        }
    }

    Button(
        modifier = Modifier
            .height(40.dp)
            .width(130.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black
        ),
        onClick = { viewModel.login(context) }
    ) {
        if(viewModel.isLoading.value){
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = "Login",
                color = Color.White
            )
        }
    }
}

@Composable
fun SignupText(
    navController: NavHostController,
    function: () -> Unit
){
    Row {
        Text(
            text = "Don't have an account?   ",
            color = Color.Black
        )

        Text(
            text = "Sign Up",
            color = Color.Blue,
            modifier = Modifier.clickable {
                function()
            }
        )
    }
}