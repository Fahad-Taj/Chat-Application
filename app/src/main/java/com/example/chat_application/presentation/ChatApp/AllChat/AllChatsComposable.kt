package com.example.chat_application.presentation.ChatApp.AllChat

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.models.Chat
import com.example.chat_application.models.DirectChatsResponse
import com.example.chat_application.models.User
import com.example.chat_application.presentation.ChatApp.ChatRoutes
import com.example.chat_application.util.Unread_Message_Count
import com.example.chat_application.util.User_Guid

@RequiresApi(Build.VERSION_CODES.O)
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
        val isTyping by viewModel.isTyping.collectAsState()
        val isStatus by viewModel.isStatus.collectAsState()

        var chats: DirectChatsResponse? = null
        val DirectChats by viewModel.chats.collectAsState()
        DirectChats?.let { response ->
            if (response.isSuccessful) {
                chats = response.body()
                Unread_Message_Count = chats!!.total_unread_messages_count
            } else {
                //show the error
            }
        } ?: run {
            Text("Loading the state")
        }



        LaunchedEffect(key1 = Unit) {
            viewModel.getDirectChats()
            viewModel.selectedScreen = null
        }
        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            chats?.chats?.forEach { it ->
                Surface(
                    modifier = Modifier
                        .shadow(9.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    SingleUserRow(
                        chat = it,
                        navController = navController,
                        viewModel = viewModel,
                        isTyping,
                        isStatus
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))

            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleUserRow(
    chat: Chat,
    navController: NavHostController,
    viewModel: ChatViewModel,
    isTyping: Map<String, Boolean>,
    isActive: Map<String, Boolean>
) {
    val user: User? = chat.users.find { it.guid != User_Guid }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
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
                .background(Color(0xff00008B)),
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
                    Text(text = user!!.username, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    if (isTyping[user.guid] == true) {
                        Text(
                            text = "is typing ....",
                            color = Color(0xff2F6030),
                            fontSize = 10.sp
                        )
                    }
                }
                if (chat.new_messages_count > 0) {
                    Text(
                        text = "${chat.new_messages_count}",
                        color = Color(0xff2F6030),
                        fontSize = 10.sp
                    )
                }
                if (user != null) {
                    if (isActive[user.guid] == true) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Green),
                        )
                    }
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AllChatsComposable(navController = rememberNavController(), viewModel = ChatViewModel())
}