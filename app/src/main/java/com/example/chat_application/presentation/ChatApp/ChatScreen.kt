package com.example.chat_application.presentation.ChatApp

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.models.Message
import com.example.chat_application.models.OnReceived.WebSocketMessage
import com.example.chat_application.models.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel, navController: NavHostController
) {
    Log.e("Chat from chat screen", viewModel.selectedScreen.toString())
    val chat = viewModel.selectedScreen
    val messages = viewModel.messages.collectAsState()
    val sender = chat?.users?.get(0)
    val chat_guid = chat?.chat_guid
    val receiver = chat?.users?.get(1)

    val receivedMessage by viewModel.receivedMessages.collectAsState()

    BackHandler {
        viewModel.selectedScreen = null
        navController.navigate(ChatRoutes.AllChats.route){
            popUpTo(ChatRoutes.AllChats.route){inclusive = true}
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.disconnectWebSocket()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.connectWebSocket()
        if (chat_guid != null) {
            viewModel.getMessages(chat_guid)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        receiver?.let {
            TopBar(it)
        }
    }, bottomBar = {
        if (chat_guid != null && sender?.guid != null) {
            BottomBar(viewModel = viewModel, sender.guid, chat_guid)
        }
    }) { paddingValues ->
//        // All communication will take place here
//        Column {
//            when (receivedMessage) {
//                is WebSocketMessage.NewMessage -> {
//                    val message = receivedMessage as WebSocketMessage.NewMessage
//                    Log.e("receivedMessage",message.content)
//                }
//
//                is WebSocketMessage.UserTyping -> {
//                    val typing = receivedMessage as WebSocketMessage.UserTyping
//                    Log.e("receivedMessage",typing.type)
//
//                }
//
//                is WebSocketMessage.StatusMessage -> {
//                    val status = receivedMessage as WebSocketMessage.StatusMessage
//                    Log.e("receivedMessage",status.status)
//                }
//
//                is WebSocketMessage.MessageRead -> {
//                    val MessageRead = receivedMessage as WebSocketMessage.MessageRead
//                    Log.e("receivedMessage",MessageRead.last_read_message_guid)
//
//                }
//                null -> {
//                    Log.e("receivedMessage","null")
//                }
//            }
//        }
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xffD3D3D3))
        ) {
            // zoned time
            var last_date = ""
            val displayFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
            val zonedDateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

            val sorted_messages = messages.value.messages.sortedBy {
                it.created_at
//                val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
//                ZonedDateTime.parse(it.created_at, formatter)
            }


            items(sorted_messages) { message ->

                val isSender: Boolean = message.user_guid == sender?.guid
                Log.e("From Lazy Column: isSender  ", isSender.toString() )

                val messageDate = ZonedDateTime.parse(message.created_at, zonedDateTimeFormatter)
                val formattedDate = messageDate.format(displayFormatter)

                if (last_date != formattedDate) {
                    Text(
                        text = formattedDate,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    last_date = formattedDate
                }

                MessageItem(message, isSender)
            }
        }
    }
}


@Composable
fun TopBar(user: User) {
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
                    .size(45.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = user.username)
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(viewModel: ChatViewModel, user_guid: String, chatGuid: String) {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(50.dp),
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
            modifier = Modifier
                .height(30.dp)
                .width(60.dp)
                .clickable {
                    if (viewModel.msg.value.isNotBlank()) {
                        viewModel.sendMessage(viewModel.msg.value, user_guid, chatGuid)
                    }
                },
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = null,
            tint = Color.Black
        )
    }


}

@Preview(showBackground = true, showSystemUi = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarPreview() {
    ChatScreen(viewModel = ChatViewModel(), navController = rememberNavController())

}

@Composable
fun MessageItem(message: Message, isSender: Boolean) {
    Log.e("From message item: Message", message.content.toString())
    Log.e("From message item: isSender", isSender.toString())
    val alignment = if (isSender) {
        Alignment.End // Align to the end for sender
    } else {
        Alignment.Start // Align to the start for receiver
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Adjust padding as needed
        horizontalArrangement = if (isSender) {
            Arrangement.End // Align to the end for sender
        } else {
            Arrangement.Start // Align to the start for receiver
        }
    ) {
        Surface(
            modifier = Modifier.shadow(5.dp).clip(RoundedCornerShape(4.dp))
        ) {
            Text(
                text = message.content,
                modifier = Modifier // Different background color
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)) // Align the text based on sender or receiver
            )
        }
    }
}
