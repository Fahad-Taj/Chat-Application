package com.example.redchat.presentation.main.chat_screen

import android.graphics.Paint.Align
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.redchat.models.Friend
import com.example.redchat.models.Message
import com.example.redchat.presentation.main.MainViewModel
import kotlin.reflect.typeOf

@Composable
fun ChatScreen(
    navController: NavHostController,
    friend: Friend?,
    viewModel: MainViewModel
){
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val chatViewmodel: ChatViewmodel = remember { ChatViewmodel(viewModel) } // Use remember to retain state across recompositions
    val messages by remember { mutableStateOf(chatViewmodel.messages) } // Listen to changes in messages
    val items = chatViewmodel.items?.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    // LaunchedEffect to handle scroll position when messages change
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            // Scroll to the bottom (which is index 0 due to reverseLayout)
            listState.animateScrollToItem(0)
        }
    }

    DisposableEffect(Unit) {
        chatViewmodel.listenToEvent("receiveMessage")

        onDispose {
            chatViewmodel.leaveRoom()
            chatViewmodel.stopListeningToEvent("receiveMessage")
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(top = statusBarHeight),
        topBar = {
            TopChatBar(friend = friend)
        },
        bottomBar = {
            BottomBar(viewModel = chatViewmodel)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(horizontal = 15.dp),
            reverseLayout = true,  // Change this back to true
            verticalArrangement = Arrangement.Bottom,  // Add this line
            state = listState
        ) {
            items(messages) { message ->
                Spacer(modifier = Modifier.height(10.dp))
                MessageItem(message, viewModel.user.userId)
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (items != null) {
                items(items.itemCount) { index ->
                    Spacer(modifier = Modifier.height(10.dp))
                    items[index]?.let { it1 -> SecondMessageItem(it1, viewModel.user.userId) }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

// Scroll to the bottom when a new message is added
        // Scroll to the bottom when a new message is added
        // Scroll to the bottom when a new message is added
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(0)  // Scroll to the first item (bottom in reversed layout)
            }
        }
    }
}

@Composable
fun SecondMessageItem(message: com.example.redchat.models.loadChat.Message, userId: String){
    val message = Message(
        content = message.content,
        contentType = message.contentType,
        senderId = message.senderId
    )
    MessageItem(message = message, userId = userId)
}

@Composable
fun MessageItem(message: Message, userId: String){
    if(message.senderId == userId){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(modifier = Modifier
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp, vertical = 5.dp)
                .clip(RoundedCornerShape(10.dp)),text = message.content)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp, vertical = 5.dp)
                    .clip(RoundedCornerShape(10.dp)),
                text = message.content
            )
        }
    }
}

@Composable
fun TopChatBar(friend: Friend?){
    Surface(modifier = Modifier.shadow(9.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if(friend != null){
                AsyncImage(
                    model = friend.photo,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = friend.username)
            } else {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Temp User")
            }

        }
    }

}

@Composable
fun BottomBar(viewModel: ChatViewmodel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = viewModel.message.value,
            onValueChange = {
                viewModel.message.value = it
                viewModel.emitUserTyping()
            })
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            viewModel.sendMessage()
        }) {
            Text(text = "Send")
        }

    }
}