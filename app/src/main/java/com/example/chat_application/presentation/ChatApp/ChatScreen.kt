package com.example.chat_application.presentation.ChatApp

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chat_application.models.Chat
import com.example.chat_application.models.User

@Composable
fun ChatScreen(
    chat: Chat,
    navController: NavHostController
){

    Log.e("Chat", chat.toString())
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(user = chat.users[1])
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            
        }
    }
}

@Composable
fun TopBar(user: User){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Icon(modifier = Modifier.size(40.dp), imageVector = Icons.Filled.AccountCircle, contentDescription = null, tint = Color.Black)
        Spacer(modifier = Modifier.width(40.dp))
        Text(text = user.username, style = MaterialTheme.typography.labelSmall)
    }
}