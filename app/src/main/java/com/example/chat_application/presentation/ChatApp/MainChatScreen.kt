package com.example.chat_application.presentation.ChatApp

import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.example.chat_application.presentation.Root_graph_routes
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainChatScreen(navController: NavHostController){

    val navController = rememberNavController()
    val viewModel: ChatViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(key1 = Unit) {
        viewModel.selectedScreen = null
    }

    Column(
        modifier = Modifier.fillMaxSize().navigationBarsPadding()
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
            Text(text = "Chat Application", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // Row containing the tabs

        if(viewModel.selectedScreen == null){
            Surface(
                modifier = Modifier.shadow(9.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    val selectedAll = currentDestination?.hierarchy?.any { it.route == ChatRoutes.AllChats.route } == true
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(80.dp)
                            .wrapContentSize(Alignment.Center)
                            .clickable { navController.navigate(ChatRoutes.AllChats.route) },
                        text = "All",
                        textDecoration = if(selectedAll)   TextDecoration.Underline else TextDecoration.None
                    )

                    val selectedRequested = currentDestination?.hierarchy?.any { it.route == ChatRoutes.RequestedChats.route } == true
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(80.dp)
                            .wrapContentSize(Alignment.Center)
                            .clickable { navController.navigate(ChatRoutes.RequestedChats.route) },
                        text = "Requested",
                        textDecoration = if(selectedRequested)   TextDecoration.Underline else TextDecoration.None
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
fun MainChatScreenPreview(){
    MainChatScreen(navController = rememberNavController())
}