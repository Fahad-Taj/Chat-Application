package com.example.redchat.presentation.main.main_screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.redchat.models.Friend
import com.example.redchat.models.FriendRequest
import com.example.redchat.presentation.main.MainViewModel

@Composable
fun AcceptFriendRequestDialog(viewModel: MainViewModel){
    var openFriendReqDialog by remember { mutableStateOf(false) }
    var openAddFriendDialog by remember { mutableStateOf(false) }
    val friendReqList = viewModel.user.friendRequests
    println("Entered AcceptFriendRequestDialog")
    Row(

    ) {
        println("Row Entered")
        Icon(
            modifier = Modifier
                .size(35.dp)
                .clickable { openFriendReqDialog = true },
            imageVector = Icons.Default.AddCircle,
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .size(35.dp)
                .clickable { openAddFriendDialog = true },
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null
        )
    }

    // Button to check friend requests
    FriendRequestDialog(
        openDialog = openFriendReqDialog,
        function = {
            openFriendReqDialog = false
        },
        friendReqList = friendReqList,
        viewModel = viewModel
    )

    // Button to add friends
    AddFriendDialog(
        openDialog = openAddFriendDialog,
        function = {
            openAddFriendDialog = false
        }
    )

}

@Composable
fun AddFriendDialog(
    openDialog: Boolean,
    function: () -> Unit
){
    val context = LocalContext.current

    var viewModel: AddFriendDialogViewmodel = viewModel()
    var username by remember { mutableStateOf("") }
    // Dialog implementation
    if (openDialog) {
        Dialog(onDismissRequest = { function() }) {
            Surface(
                modifier = Modifier
                    .width(350.dp)
                    .height(500.dp),
                shape = MaterialTheme.shapes.small,
            ) {
                // This is what will be rendered inside the dialog
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    OutlinedTextField(
                        value = viewModel.username,
                        onValueChange = {
                            viewModel.username = it
                        },
                        label = {
                            Text(text = "Username")
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Send friend request button
                    Button(
                        shape = RoundedCornerShape(4.dp),
                        onClick = { viewModel.sendFriendRequest(context) }) {
                        Text(text = "Send friend Req")
                    }
                }


            }

        }
    }
}

@Composable
fun FriendRequestDialog(
    openDialog: Boolean,
    function: () -> Unit,
    friendReqList: List<FriendRequest>,
    viewModel: MainViewModel
){
    // Dialog implementation
    if (openDialog) {
        Dialog(onDismissRequest = { function() }) {
            Surface(
                modifier = Modifier
                    .width(350.dp)
                    .height(500.dp),
                shape = MaterialTheme.shapes.small,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    friendReqList.forEach {
                        FriendRequestComposable(viewModel = viewModel, friend = it)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                }

            }

        }
    }
}

// Single Row per friend request
@Composable
fun FriendRequestComposable(viewModel: MainViewModel, friend: FriendRequest){
    // Surface wrapper for Shadow
    val context = LocalContext.current
    val localViewModel: FriendRequestViewmodel = FriendRequestViewmodel(viewModel)

    Surface(modifier = Modifier.shadow(5.dp)) {
        // Main Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // First Row contains the username and DP
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Friend's dp
                println(friend.user.photo)

                if(friend.user.photo == null){
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Black, CircleShape)
                    )
                } else {
                    AsyncImage(
                        model = friend.user.photo,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Black, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))

                // Friend Username
                Text(text = friend.user.username, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(10.dp))
            }

            // Second Row, contains buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Accept button
                Button(onClick = {
                    localViewModel.acceptFriendRequest(viewModel, friend.user.username, context)
                }) {
                    Text(text = "Accept")
                }
                Spacer(modifier = Modifier.width(10.dp))

                // Reject button
                Button(onClick = {
                    localViewModel.rejectFriendRequest(viewModel, friend.user.username, context)
                }) {
                    Text(text = "Reject")
                }
            }
        }
    }

}