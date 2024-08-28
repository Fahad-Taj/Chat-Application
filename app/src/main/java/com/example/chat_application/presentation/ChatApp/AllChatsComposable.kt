package com.example.chat_application.presentation.ChatApp

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.R
import com.example.chat_application.api.RetrofitInstance
import com.example.chat_application.models.Chat
import com.example.chat_application.models.User
import com.example.chat_application.models.UserItem
import com.example.chat_application.util.user_details

@Composable
fun AllChatsComposable(
    navController: NavHostController,
    viewModel: ChatViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        val chatList by viewModel.chatList.collectAsState()
        // we can eaily get the count here but how can we update it in the real time that is the problem maybe socket htemselves do that
//        val new_message_count=chatList.size.

        LaunchedEffect(key1 = Unit) {
            viewModel.getDirectChats()
            viewModel.selectedScreen = null
        }
        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            chatList.forEach { it ->
                Surface(
                    modifier = Modifier
                        .shadow(9.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    SingleUserRow(chat = it, navController = navController, viewModel = viewModel)
                }
                Spacer(modifier = Modifier.height(5.dp))

            }
        }

    }
}

@Composable
fun SingleUserRow(
    chat: Chat,
    navController: NavHostController,
    viewModel: ChatViewModel,
    isTyping: MutableState<Boolean> = mutableStateOf(true),
    isActive: MutableState<Boolean> = mutableStateOf(true)
) {
    val user: User = chat.users[1]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(10.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .clickable {
                viewModel.selectedScreen = chat
                navController.navigate(ChatRoutes.ChatScreen.route)
            },
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = user.username, style = MaterialTheme.typography.labelSmall)
                    if (isTyping.value) {
                        Text(
                            text = "is Typing ...",
                            color = Color(0xff2F6030),
                            fontFamily = FontFamily(Font(R.font.matemasie_regular)),
                            fontSize = 10.sp
                        )
                    }
                }
                if (isActive.value) {
                    Spacer(modifier = Modifier.width(20.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.Green),
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Composable
fun RequestedChatsComposable(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Requested Chats Screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AllChatsComposable(navController = rememberNavController(), viewModel = ChatViewModel())
}