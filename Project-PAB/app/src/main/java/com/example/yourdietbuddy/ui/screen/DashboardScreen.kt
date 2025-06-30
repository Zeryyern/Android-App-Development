package com.example.yourdietbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen() {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFF5F7FA), Color(0xFFC3CFE2))
    )

    val meals = listOf(
        Meal("07:30", "Sarapan", "Pancake + Madu + Buah", 420),
        Meal("12:15", "Makan Siang", "Nasi + Ayam Bakar + Sayur", 650),
        Meal("15:00", "Snack", "Apel + Kacang Almond", 180),
        Meal("19:30", "Makan Malam", "Salad Sayuran + Protein", 320)
    )

    Scaffold(
        bottomBar = { DashboardBottomBar() },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Progress Hari Ini",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF2D3748),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        "26 Mei 2025",
                        fontSize = 16.sp,
                        color = Color(0xFF4A5568),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        textAlign = TextAlign.Start
                    )
                    CalorieProgressSection()
                    Spacer(Modifier.height(12.dp))
                    MacroSection()
                    Spacer(Modifier.height(20.dp))
                    ActionCards()
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Makanan Hari Ini",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF2D3748),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            "Lihat Semua",
                            color = Color(0xFF3182CE),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clickable { /* TODO: Show all meals */ }
                        )
                    }
                }
                items(meals) { meal ->
                    MealCard(meal)
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun CalorieProgressSection() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("1,420", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Color(0xFF38A169))
            Text("dari 2,000 kal", color = Color(0xFF4A5568), fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text("580 kal tersisa", color = Color(0xFF3182CE), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun MacroSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MacroCard("85g", "Protein", Color(0xFF805AD5))
        MacroCard("180g", "Karbohidrat", Color(0xFF38A169))
        MacroCard("35g", "Lemak", Color(0xFFE53E3E))
    }
}

@Composable
fun MacroCard(amount: String, label: String, color: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.13f)),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .width(100.dp)
            .height(60.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(amount, fontWeight = FontWeight.Bold, color = color, fontSize = 18.sp)
            Text(label, color = color, fontSize = 13.sp)
        }
    }
}

@Composable
fun ActionCards() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionCard(
            emoji = "🍽️",
            title = "Tambah Makanan",
            subtitle = "Catat makanan yang baru saja Anda konsumsi"
        )
        ActionCard(
            emoji = "\uD83D\uDCA7", // 💧 Water drop emoji
            title = "Catat Air",
            subtitle = "Pantau asupan air harian Anda"
        )
    }
}

@Composable
fun ActionCard(emoji: String, title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .width(160.dp)
            .height(90.dp)
            .padding(end = 6.dp)
            .clickable { /* TODO: Action */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4ECDC4)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 20.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF4A5568))
            }
        }
    }
}

data class Meal(
    val time: String,
    val type: String,
    val description: String,
    val calories: Int
)

@Composable
fun MealCard(meal: Meal) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(meal.time, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF805AD5))
                Text(meal.type, fontSize = 12.sp, color = Color(0xFF4A5568))
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(meal.description, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                Text("${meal.calories} kal", color = Color(0xFF38A169), fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun DashboardBottomBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(60.dp)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
            label = { Text("Beranda", fontSize = 12.sp) },
            selected = true,
            onClick = { /* TODO: Beranda */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Diary") },
            label = { Text("Diary", fontSize = 12.sp) },
            selected = false,
            onClick = { /* TODO: Diary */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Tambah") },
            label = { Text("Tambah", fontSize = 12.sp) },
            selected = false,
            onClick = { /* TODO: Tambah */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
            label = { Text("Profil", fontSize = 12.sp) },
            selected = false,
            onClick = { /* TODO: Profil */ }
        )
    }
}
