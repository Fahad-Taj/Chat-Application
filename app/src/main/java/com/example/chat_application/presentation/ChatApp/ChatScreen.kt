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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.chat_application.models.Message
import com.example.chat_application.models.User
import com.example.chat_application.presentation.ChatApp.AllChat.ChatViewModel
import com.example.chat_application.util.Chat_Guid
import com.example.chat_application.util.User_Guid
import java.time.LocalDate
import java.time.ZoneId
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
    val sender = chat?.users?.find { it.guid == User_Guid }
    val chat_guid = chat?.chat_guid
    Chat_Guid = chat_guid

    val receiver = chat?.users?.find { it.guid != User_Guid }

    val isTyping by viewModel.isTyping.collectAsState()
    val isStatus by viewModel.isStatus.collectAsState()

    val receivedMessage by viewModel.receivedMessages.collectAsState()

    val connectionStatus by viewModel.connectionStatus.collectAsState()

    BackHandler {
        viewModel.selectedScreen = null
        navController.navigate(ChatRoutes.AllChats.route) {
            popUpTo(ChatRoutes.AllChats.route) { inclusive = true }
        }
    }



    LaunchedEffect(Unit) {
        if (chat_guid != null) {
            viewModel.getMessages(chat_guid)
        }
        Log.e("demo", chat?.users.toString())

    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        receiver?.let {
            TopBar(it, isTyping, isStatus)
        }
    }, bottomBar = {
        if (chat_guid != null && sender?.guid != null) {
            BottomBar(viewModel = viewModel, sender.guid, chat_guid)
        }
    }) { paddingValues ->

        when (connectionStatus) {
            "connected" -> {

                //control the date part and pagination is pending
//                LaunchedEffect(sorted_messages) {
//                    listState.scrollToItem(sorted_messages.size)
//                }
                val listState = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(Color(0xffD3D3D3)),
                    reverseLayout = true,
                    state = listState
                ) {
                    var last_date = ""
                    val displayFormatterSameYear = DateTimeFormatter.ofPattern("EEEE d MMM")
                    val displayFormatterDifferentYear =
                        DateTimeFormatter.ofPattern("EEEE d MMM yyyy")
                    val zonedDateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
                    val currentYear = LocalDate.now().year

                    val sorted_messages =
                        messages.value.messages.sortedByDescending { it.created_at }

                    items(sorted_messages) { message ->
                        if (message.is_read == false) {
                            viewModel.sendMessageReadEvent(
                                message.chat_guid, message.message_guid
                            )
                        }
                        val isSender: Boolean = message.user_guid == sender?.guid
                        val messageDate =
                            ZonedDateTime.parse(message.created_at, zonedDateTimeFormatter)

                        // Format the date based on whether it's from the current year or not
                        val formattedDate = if (messageDate.year == currentYear) {
                            messageDate.format(displayFormatterSameYear)
                        } else {
                            messageDate.format(displayFormatterDifferentYear)
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (last_date != formattedDate) {
                                Text(
                                    text = formattedDate,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                                last_date = formattedDate
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            MessageItem(message, isSender)
                        }
                    }
                }
            }

            "disconnected" -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Web Socket Disconnected")
                }
            }

            else -> {
                //else
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Something Error Happened")
                }
            }
        }

    }
}


@Composable
fun TopBar(
    user: User, isTyping: Map<String, Boolean>, isActive: Map<String, Boolean>
) {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = user.username, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    if (isTyping[user.guid] == true) {
                        Text(
                            text = "is Typing ...", color = Color(0xff2F6030), fontSize = 10.sp
                        )
                    }
                }
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

            Spacer(modifier = Modifier.width(40.dp))

            MyDropDown()
        }
    }

}

@Composable
fun MyDropDown() {

    var expanded by remember { mutableStateOf(false) }

    Icon(imageVector = Icons.Default.MoreVert,
        contentDescription = null,
        modifier = Modifier
            .size(24.dp)
            .clickable {
                expanded = true
            })
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(text = { Text("Delete") }, onClick = {

            expanded = false
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
//    ChatScreen(viewModel = ChatViewModel(), navController = rememberNavController())
//    TopBar(user = User("ddd", "dddd", "ddddd", "null", "dddddddd"), true, true)
//    BottomBar(viewModel = ChatViewModel(), user_guid = "sscsjfnd", chatGuid = "jdjdj")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageItem(message: Message, isSender: Boolean) {
    val arrangement: Arrangement.Horizontal = if (isSender) {
        Arrangement.End // Align to the end for sender
    } else {
        Arrangement.Start // Align to the start for receiver
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Adjust padding as needed
        horizontalArrangement = arrangement
    ) {
        Surface(
            modifier = Modifier
                .shadow(5.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Text(
                text = message.content, modifier = Modifier // Different background color
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)) // Align the text based on sender or receiver
            )
        }
        val time = extractTimeFromTimestamp(message.created_at)
        Text(text = time, fontSize = 12.sp)
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            imageVector = Icons.Default.DoneAll,
            contentDescription = "Message Read",
            tint = if (message.is_read) Color.Blue else Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun extractTimeFromTimestamp(timestamp: String): String {
    val zonedDateTime = ZonedDateTime.parse(timestamp)
    val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return localDateTime.format(formatter)
}