package com.example.chat_application.presentation.ChatApp.RequestedChat

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.chat_application.R
import com.example.chat_application.models.GetAllUsers.GetAllUsersItem
import com.example.chat_application.presentation.ChatApp.AllChat.ChatViewModel
import com.example.chat_application.presentation.ChatApp.AllChat.SingleUserRow
import com.example.chat_application.presentation.ChatApp.ChatRoutes
import com.example.chat_application.util.baseUrl
import org.jetbrains.annotations.Async

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestedChatsComposable(
    rootViewModel: ChatViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val viewModel: RequestedChatVideModel = viewModel()
        val allUsers by viewModel.allUsers.collectAsState()


        Scaffold(
            topBar = {
            }
        ) {paddingvalues->
            // Show the list of users
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(paddingvalues)) {
                items(allUsers.size) {index ->
                    val user = allUsers[index]
                    UserRow(user, navController = navController, viewModel = rootViewModel)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
//            allUsers.forEach { it ->
//                Surface(
//                    modifier = Modifier
//                        .shadow(9.dp)
//                        .clip(RoundedCornerShape(10.dp))
//                ) {
//                    SingleUserRow(chat = it, navController = navController, viewModel = viewModel)
//                }
//                Spacer(modifier = Modifier.height(5.dp))
//
//            }

        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserRow(user: GetAllUsersItem,navController: NavHostController,viewModel: ChatViewModel) {
    Card(modifier = Modifier.border(2.dp,Color.Black, shape = RectangleShape)){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { viewModel.createDirectChat(navController, user.guid) }
        ) {
            // User Image
            if (user.user_image != null) {
                Log.e("image", user.user_image.toString())
                val newurl=user.user_image.toString().replace("http://localhost:8001", baseUrl)
                val painter = rememberImagePainter(
//                    data = "http://192.168.0.109:8001/static/images/profile/hi.jpeg",
                    data=newurl
                )
                Image(
                    painter = painter,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User Information
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${user.first_name} ${user.last_name}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "@${user.username}",
                    color = Color.Gray
                )
                Text(
                    text = user.email,
                )
            }
        }
    }
}
