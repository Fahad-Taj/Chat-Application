package com.example.chat_application.presentation.Authentication.signup

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.chat_application.presentation.Authentication.AuthenticationRoutes
import com.example.chat_application.presentation.ChatApp.ChatRoutes
import com.example.chat_application.presentation.Root_graph_routes
import com.example.chat_application.ui.theme.primary_font

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpComposable(
    navController: NavHostController,
    viewModel: SignupViewmodel = androidx.lifecycle.viewmodel.compose.viewModel()
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
        viewModel.error_message.value = ""

    }

    LaunchedEffect(key1 = response) {
        if(response != null){
            Toast.makeText(context, "User successfully created, please login to continue", Toast.LENGTH_SHORT).show()
            navController.navigate(AuthenticationRoutes.Login.route){
                popUpTo(AuthenticationRoutes.Login.route) { inclusive = true }
            }
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
            
            DisplayImageFromUri(uri = viewModel.uri.value)
            
            // Column containing the Text Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.9f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // First Name field
                OutlinedTextField(
                    value = viewModel.firstName.value,
                    onValueChange = {
                        viewModel.firstName.value = it
                    },
                    label = { Text("First Name") },
                    //visualTransformation = PasswordVisualTransformation(),
                )
                Spacer(modifier = Modifier.height(15.dp))

                // Last Name field
                OutlinedTextField(
                    value = viewModel.lastName.value,
                    onValueChange = {
                        viewModel.lastName.value = it
                    },
                    label = { Text("Last Name") },
                )
                Spacer(modifier = Modifier.height(15.dp))

                // Username text field
                OutlinedTextField(
                    value = viewModel.username.value,
                    onValueChange = {
                        viewModel.username.value = it
                    },
                    label = { Text("Username") },
                )
                Spacer(modifier = Modifier.height(15.dp))

                // Email text field
                OutlinedTextField(
                    value = viewModel.email.value,
                    onValueChange = {
                        viewModel.email.value = it
                    },
                    label = { Text("Email") },
                )
                Spacer(modifier = Modifier.height(15.dp))

                // Password text field
                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = {
                        viewModel.password.value = it
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                Spacer(modifier = Modifier.height(30.dp))

                // SignUp Button
                Button(
                    modifier = Modifier
                        .height(70.dp)
                        .width(170.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = { viewModel.register() }
                ) {
                    if(!viewModel.isWaiting.value){
                        Text(text = "Signup")
                    } else {
                        CircularProgressIndicator(modifier = Modifier.size(30.dp),color = Color.White)
                    }

                }
                Spacer(modifier = Modifier.height(70.dp))

                // Text to navigate
                Row {
                    Text(text = "Already have an account?  ")
                    Text(
                        text = "Login",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            navController.navigate(AuthenticationRoutes.Login.route){
                                popUpTo(AuthenticationRoutes.SignUp.route){ inclusive = true }
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

@Composable
fun DisplayImageFromUri(uri: Uri?, modifier: Modifier = Modifier) {
    uri?.let {
        AsyncImage(
            model = it,
            contentDescription = null, // Provide a content description for accessibility
            modifier = modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape),
            contentScale = ContentScale.Crop,
            //contentScale = ContentScale.Crop // Choose the scale type
        )
    }
}
