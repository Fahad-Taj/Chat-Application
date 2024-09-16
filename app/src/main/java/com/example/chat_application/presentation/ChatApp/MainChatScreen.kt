package com.example.chat_application.presentation.ChatApp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.R
import com.example.chat_application.presentation.ChatApp.AllChat.ChatViewModel
import com.example.chat_application.util.Unread_Message_Count

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainChatScreen(MainNavController: NavHostController) {

    val navController = rememberNavController()
    val viewModel: ChatViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(key1 = Unit) {
        viewModel.selectedScreen = null
        viewModel.connectWebSocket()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Black)
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chat Application",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(90.dp))
            Icon(painter = painterResource(id = R.drawable.outline_logout_24),
                contentDescription = "Signout",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
//                        viewModel.disconnectWebSocket()
//                        User_Guid = null
                        //basically this should happen at the time of logout that
                        // socket is closed and navigate back

//                        navController.navigate(Root_graph_routes.AuthGraph.route) {
//                            popUpTo(Root_graph_routes.AuthGraph.route) { inclusive = true }
//                        }
                    })

        }

        // Row containing the tabs

        if (viewModel.selectedScreen == null) {
            Surface(
                modifier = Modifier.shadow(9.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val selectedAll =
                        currentDestination?.hierarchy?.any { it.route == ChatRoutes.AllChats.route } == true
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(80.dp)
                            .wrapContentSize(Alignment.Center)
                            .clickable { navController.navigate(ChatRoutes.AllChats.route) },
                        text = if(Unread_Message_Count>0) "Your Chat$ ${Unread_Message_Count}" else "Your Chats",
                        textDecoration = if (selectedAll) TextDecoration.Underline else TextDecoration.None
                    )

                    val selectedRequested =
                        currentDestination?.hierarchy?.any { it.route == ChatRoutes.RequestedChats.route } == true
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(80.dp)
                            .wrapContentSize(Alignment.Center)
                            .clickable { navController.navigate(ChatRoutes.RequestedChats.route) },
                        text = "All Users",
                        textDecoration = if (selectedRequested) TextDecoration.Underline else TextDecoration.None
                    )
                }
            }
            HorizontalDivider()
        }

        ChatGraph(navController = navController, chatViewModel = viewModel)

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainChatScreenPreview() {
    MainChatScreen(rememberNavController())
}