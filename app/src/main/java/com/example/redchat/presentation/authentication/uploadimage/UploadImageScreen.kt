package com.example.redchat.presentation.authentication.uploadimage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.redchat.presentation.authentication.AuthGraphRoutes
import com.example.redchat.presentation.main.MainGraphRoutes

@Composable
fun UploadImageScreen(
    viewModel: UploadImageViewmodel,
    navController: NavHostController
){

    // Main Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Header
        UploadImageHeader()
        Spacer(modifier = Modifier.height(20.dp))

        // Composable that will display the selected image
        DisplayImageFromUri(uri = viewModel.uri.value)
        Spacer(modifier = Modifier.height(20.dp))

        // Button to select image
        FilePicker(
            onFileSelected = { uri ->
                viewModel.uri.value = uri
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Button to Upload file
        ImageUploader(viewModel = viewModel, navController = navController)
    }

}

@Composable
fun UploadImageHeader(){
    Text(
        text = "Upload your profile image",
        fontSize = 25.sp,
        fontWeight = FontWeight.SemiBold
    )
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

@Composable
fun FilePicker(onFileSelected: (Uri?) -> Unit) {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedFileUri = uri
            onFileSelected(uri)
        }
    )

    Button(
        modifier = Modifier
            .height(40.dp)
            .width(150.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
           backgroundColor = Color.Black
        ),
        onClick = {
        launcher.launch("image/*") // Use "*/*" to allow picking any file type
    }) {
        Text(text = "Pick an image", color = Color.White)
    }
}

@Composable
fun ImageUploader(
    viewModel: UploadImageViewmodel,
    navController: NavHostController
){
    val context = LocalContext.current
    val response by viewModel.response.collectAsState()

    LaunchedEffect(key1 = response) {
        response?.let {
            navController.navigate("main_nav_graph")
        }
    }

    Button(
        modifier = Modifier
            .height(40.dp)
            .width(150.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black
        ),
        onClick = { viewModel.uploadImage(context = context) }) {
            if(viewModel.isLoading.value){
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(text = "Upload Image", color = Color.White)
            }
    }
}