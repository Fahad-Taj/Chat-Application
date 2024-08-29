package com.example.chat_application.presentation.Authentication.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.chat_application.presentation.Authentication.AuthenticationRoutes
import com.example.chat_application.presentation.Root_graph_routes
import com.example.chat_application.ui.theme.primary_font
import com.example.chat_application.util.User_Guid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComposable(
    navController: NavHostController,
    viewModel: LoginViewmodel = androidx.lifecycle.viewmodel.compose.viewModel(),
    state: LoginScreenState
    ){

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val response by viewModel.response.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = viewModel.state.value.hasInternetConnection) {
        viewModel.checkInternetConnection(context, lifecycleOwner)
    }

    LaunchedEffect(key1 = viewModel.error_message.value){
        if(viewModel.error_message.value != ""){
            Toast.makeText(context, viewModel.error_message.value, Toast.LENGTH_SHORT).show()
        }

    }

    LaunchedEffect(key1 = response) {
        if(response != null){
            User_Guid= response!!.user_guid
            navController.navigate(Root_graph_routes.MainScreenRoute.route)
        }
    }



    if(!viewModel.state.value.hasInternetConnection){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Can't connect to the internet")
        }
    } else {
        // Main Column, will contain the entire screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Column containing the Text Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.9f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Username text field
                OutlinedTextField(
                    value = viewModel.username.value,
                    onValueChange = {
                        viewModel.username.value = it
                    },
                    label = { Text("Username / Email", fontWeight = FontWeight.SemiBold) }
                )
                Spacer(modifier = Modifier.height(20.dp))


                // Password text field
                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = {
                        viewModel.password.value = it
                    },
                    label = { Text("Password", fontWeight = FontWeight.SemiBold) }
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Login Button
                Button(
                    modifier = Modifier
                        .height(70.dp)
                        .width(170.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = { viewModel.login() }
                ) {
                    if(!viewModel.isWaiting.value){
                        Text(text = "Login")
                    } else {
                        CircularProgressIndicator(modifier = Modifier.size(30.dp),color = Color.White)
                    }

                }
                Spacer(modifier = Modifier.height(70.dp))
                
                // Text to navigate
                Row {
                    Text(text = "Don't have an account?  ", color = MaterialTheme.colorScheme.primary)
                    Text(
                        text = "Sign Up",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            navController.navigate(AuthenticationRoutes.SignUp.route){
                                popUpTo(AuthenticationRoutes.Login.route){inclusive = true}
                            }
                    })

                }
            }

            // Column containing the logo at the bottom of the screen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Chat Application",
                    fontSize = 20.sp,
                    color = Color.White,
                )
            }

        }
    }

}