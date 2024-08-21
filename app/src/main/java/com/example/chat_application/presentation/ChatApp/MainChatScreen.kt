package com.example.chat_application.presentation.ChatApp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.R
import com.example.chat_application.presentation.Root_graph_routes
import kotlin.math.log

@Composable
fun MainChatScreen(navController: NavHostController){

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.Blue),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Chat Application", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }

        // Row containing the tabs
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
                    fontFamily = FontFamily(Font(R.font.matemasie_regular)),
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
                    fontFamily = FontFamily(Font(R.font.matemasie_regular)),
                    textDecoration = if(selectedRequested)   TextDecoration.Underline else TextDecoration.None
                )
            }
        }


        Divider()

        ChatGraph(navController = navController)

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainChatScreenPreview(){
    MainChatScreen(navController = rememberNavController())
}