package com.example.redchat.presentation.authentication.signup

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
import com.example.redchat.presentation.authentication.login.LoginButton
import com.example.redchat.presentation.authentication.login.LoginInputFields
import com.example.redchat.presentation.authentication.login.LoginViewmodel
import com.example.redchat.presentation.authentication.login.SignupText

@Composable
fun SignupScreen(
    navController: NavHostController,
    viewModel: SignupViewmodel
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
        SignupInputFields(viewModel = viewModel)
        Spacer(modifier = Modifier.height(20.dp))

        // Button to invoke login function
        SignupButton(viewModel = viewModel, context = context, navController = navController)
        Spacer(modifier = Modifier.height(50.dp))

        // Signup Text
        LoginText(navController = navController, function = {
            navController.navigate(AuthGraphRoutes.LoginScreen.route){
                popUpTo(AuthGraphRoutes.LoginScreen.route){ inclusive = true }
            }
        })
    }
}

@Composable
fun SignupInputFields(
    viewModel: SignupViewmodel
){
    // Email field
    OutlinedTextField(
        value = viewModel.email.value,
        onValueChange = {
            viewModel.email.value = it
        },
        label = { Text(text = "Email") }
    )
    Spacer(modifier = Modifier.height(10.dp))

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
fun SignupButton(
    context: Context,
    viewModel: SignupViewmodel,
    navController: NavHostController
){

    val response by viewModel.response.collectAsState()

    LaunchedEffect(key1 = response) {
        response?.let {
            if(access_token != null){
                navController.navigate(AuthGraphRoutes.UploadImageScreen.route){
                    popUpTo(AuthGraphRoutes.UploadImageScreen.route){ inclusive = true }
                }
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
        onClick = { viewModel.signup(context) }
    ) {
        if(viewModel.isLoading.value){
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = "Sign Up",
                color = Color.White
            )
        }
    }
}

@Composable
fun LoginText(
    navController: NavHostController,
    function: () -> Unit
){
    Row {
        Text(
            text = "Already have an acoount?   ",
            color = Color.Black
        )

        Text(
            text = "Login",
            color = Color.Blue,
            modifier = Modifier.clickable {
                function()
            }
        )
    }
}