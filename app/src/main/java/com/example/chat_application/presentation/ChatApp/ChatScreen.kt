package com.example.chat_application.presentation.ChatApp

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chat_application.models.Chat
import com.example.chat_application.models.User

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    navController: NavHostController
){
    Log.e("Chat from chat screen", viewModel.selectedScreen.toString())
    val chat = viewModel.selectedScreen
    val sender = chat?.users?.get(0)
    val receiver = chat?.users?.get(1)


    BackHandler {
        viewModel.selectedScreen = null
        navController.navigate(ChatRoutes.AllChats.route)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            receiver?.let {
                TopBar(it)
            }
        },
        bottomBar = {
            BottomBar(viewModel = viewModel)
        }
    ) { paddingValues ->
        // All communication will take place here
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            
        }
    }
}

@Composable
fun TopBar(user: User){
    Surface(
        modifier = Modifier.shadow(9.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(10.dp)
                .wrapContentHeight(Alignment.CenterVertically)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ){
                Icon(modifier = Modifier
                    .fillMaxSize(),
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = Color.Black)
            }

            Spacer(modifier = Modifier.width(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = user.username, style = MaterialTheme.typography.labelSmall)
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(viewModel: ChatViewModel){
    HorizontalDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
            value = viewModel.msg.value,
            onValueChange = { viewModel.msg.value = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Icon(
            modifier = Modifier.height(30.dp).width(60.dp),
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = null,
            tint = Color.Black
        )
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopBarPreview(){
    TopBar(
        user = User(
            username = "John",
            first_name = "John",
            last_name = "Doe",
            user_image = "John banega don",
            guid = "John Jio"
        ))

}