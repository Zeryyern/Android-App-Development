package com.example.yourdietbuddy.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val foodList = listOf("Salad", "Fruit Bowl", "Smoothie")

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Healthy Food", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        LazyColumn {
            items(foodList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("detail/$item")
                        }
                ) {
                    Text(text = item, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
