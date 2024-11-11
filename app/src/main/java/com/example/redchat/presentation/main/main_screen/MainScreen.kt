package com.example.redchat.presentation.main.main_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.redchat.models.Friend
import com.example.redchat.presentation.main.MainGraphRoutes
import com.example.redchat.presentation.main.MainViewModel
import com.example.redchat.presentation.main.SocketEvent
import kotlin.reflect.typeOf

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel
    ){

    val socketEvent by viewModel.socketEvents.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(socketEvent){
        when(socketEvent){
            is SocketEvent.Connected -> {
                Toast.makeText(context, "Websocket connected", Toast.LENGTH_SHORT).show()
                println("Socket connected")
            }

            SocketEvent.DisConnected -> {
                Toast.makeText(context, "Websocket disconnected", Toast.LENGTH_SHORT).show()
                println("Socket disconnected")
            }
            SocketEvent.Failure -> TODO()
            SocketEvent.Success -> {
                Toast.makeText(context, "Websocket success", Toast.LENGTH_SHORT).show()
                println("Socket success")
            }

            SocketEvent.ReceiveFriendRequest ->{
                Toast.makeText(context, "Friend Request received", Toast.LENGTH_SHORT).show()
            }

            else -> {
                println("Socket null")
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),
        topBar = {
            println("TopBar Entered")
            // Top-Bar Composable, it will be displayed on the top of the screen
            TopBar(viewModel = viewModel)
        },
    ) {
        println("Scaffold Entered!")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            viewModel.user.friends.forEach {friend->
                FriendCard(
                    friend = friend,
                    viewModel = viewModel,
                    navController = navController
                )
                HorizontalDivider()
            }

        }

    }

}

@Composable
fun FriendCard(
    viewModel: MainViewModel,
    friend: Friend,
    navController: NavHostController
){
    var isOnline by remember { mutableStateOf(friend.isOnline)  }

    // Effect to update the remembered state when friend.isOnline changes
    LaunchedEffect(friend.isOnline) {
        isOnline = friend.isOnline
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.selectedFriend = friend
                navController.navigate(MainGraphRoutes.ChatScreen.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(friend.photo == null){
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape)
            )
        } else {
            AsyncImage(
                model = friend.photo,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Black, CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = friend.username,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            println(println(friend::class))
            if (isOnline) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(Color.Green)
                        .clip(CircleShape)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun TopBar(viewModel: MainViewModel){
    Column(
        modifier = Modifier.padding(top = 18.dp)
    ) {
        println("Top Bar implementation entered")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "RedChat",
                fontSize = 27.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                color = Color(0xffFF4500)
            )
            println("After REDCHAT Text")
            Spacer(modifier = Modifier.width(20.dp))

            AcceptFriendRequestDialog(viewModel = viewModel)

        }
        HorizontalDivider()
    }
}



@Composable
@Preview
fun MainScreenPreview(){
    MainScreen(navController = rememberNavController(), viewModel = viewModel())
}