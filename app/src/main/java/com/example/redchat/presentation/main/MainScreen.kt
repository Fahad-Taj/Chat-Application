package com.example.redchat.presentation.main

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.redchat.models.Friend
import com.example.redchat.models.FriendRequest

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainScreenViewmodel
    ){

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),
        topBar = {
            // Top-Bar Composable, it will be displayed on the top of the screen
            TopBar(viewModel = viewModel)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Welcome to main screen")
        }

    }

}

@Composable
fun TopBar(viewModel: MainScreenViewmodel){
    Column(
        modifier = Modifier.padding(top = 18.dp)
    ) {
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
            Spacer(modifier = Modifier.width(20.dp))

            AcceptFriendRequestDialog(viewModel = viewModel)

        }
        HorizontalDivider()
    }
}

@Composable
fun AcceptFriendRequestDialog(viewModel: MainScreenViewmodel){
    var openFriendReqDialog by remember { mutableStateOf(false) }
    var openAddFriendDialog by remember { mutableStateOf(false) }
    val friendReqList = viewModel.user.friendRequests

    Row(

    ) {
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
    viewModel: MainScreenViewmodel
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
fun FriendRequestComposable(viewModel: MainScreenViewmodel ,friend: FriendRequest){
    // Surface wrapper for Shadow
    val context = LocalContext.current
    val localViewModel: FriendRequestViewmodel = viewModel()

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

@Composable
@Preview
fun MainScreenPreview(){
    MainScreen(navController = rememberNavController(), viewModel = viewModel())
}