package com.example.chat_application.presentation.ChatApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chat_application.presentation.Root_graph_routes

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){

            Button(onClick = { navController.navigate(ChatRoutes.AllChats.route) }) {
                Text(text = "All", style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.width(50.dp))

            Button(onClick = { navController.navigate(ChatRoutes.RequestedChats.route) }) {
                Text(text = "Requested", style = MaterialTheme.typography.labelSmall)
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