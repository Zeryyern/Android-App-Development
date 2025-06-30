package com.example.yourdietbuddy.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yourdietbuddy.ui.viewModel.MainViewModel

@Composable
fun DetailScreen(itemName: String, viewModel: MainViewModel = viewModel()) {
    val count by viewModel.counter.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Food: $itemName", fontSize = 22.sp)
        Text("Click Count: $count")
        Button(onClick = { viewModel.increment() }) {
            Text("Click Me")
        }
    }
}
