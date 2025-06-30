package com.example.yourdietbuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

@Composable
fun MainNavGraph(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val selectedNav = remember { mutableStateOf("🏡") }
    val recommendedFoods = remember { mutableStateOf<List<FoodItem>>(emptyList()) }
    val recommendedExercises = remember { mutableStateOf<List<ExerciseItem>>(emptyList()) }
    val bmiCategory = remember { mutableStateOf("normal") }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val authRoutes = listOf("login", "signup")
    val showHeader = currentRoute !in authRoutes
    val showBottomBar = currentRoute !in authRoutes

    Scaffold(
        topBar = {
            if (showHeader) {
                HeaderSection(onSettingsClick = {
                    // Tambahkan navigasi ke pengaturan
                })
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(selected = selectedNav, navController = navController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "login"
            ) {
                composable("login") {
                    LoginScreen(navController, profileViewModel, selectedNav = selectedNav)
                }
                composable("signup") {
                    SignUpScreen(navController, profileViewModel)
                }
                composable("dashboard") {
                    DashboardScreen(
                        recommendedFoods = recommendedFoods.value,
                        recommendedExercises = recommendedExercises.value,
                        bmiCategory = bmiCategory.value
                    )
                }
                composable("profile") {
                    ProfileScreen(
                        viewModel = profileViewModel,
                        navController = navController
                    )
                }
                composable("bmi") {
                    BMICalculatorScreen(
                        navController = navController,
                        profileViewModel = profileViewModel,
                        selectedNav = selectedNav,
                        onBMIResult = { category, foods ->
                            recommendedFoods.value = foods
                            recommendedExercises.value = getRecommendedExercises(category)
                            selectedNav.value = "🏡"

                            val targetCalories = when (category.lowercase()) {
                                "underweight" -> "2500"
                                "normal" -> "2000"
                                "overweight" -> "1600"
                                "obese" -> "1400"
                                else -> "2000"
                            }

                            profileViewModel.updateGoalTarget(
                                context = context,
                                kalori = targetCalories,
                                beratIdeal = "",
                                timeline = ""
                            )
                            navController.navigate("dashboard")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selected: MutableState<String>,
    navController: NavHostController
) {
    val navItems = listOf(
        "🏡" to "Beranda",
        "📊" to "Analisis",
        "🧑‍⚕️" to "Profil"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, clip = false)
            .background(Color.White)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        navItems.forEach { (icon, label) ->
            val isSelected = selected.value == icon
            Column(
                modifier = Modifier
                    .clickable {
                        if (!isSelected) {
                            selected.value = icon
                            val destination = when (icon) {
                                "🏡" -> "dashboard"
                                "📊" -> "bmi"
                                "🧑‍⚕️" -> "profile"
                                else -> return@clickable
                            }
                            navController.navigate(destination) {
                                popUpTo(destination) { inclusive = true }
                            }
                        }
                    }
                    .width(80.dp)
                    .padding(8.dp)
                    .then(
                        if (isSelected) Modifier.background(
                            Brush.linearGradient(
                                listOf(Color(0xFF4ECDC4), Color(0xFF44A08D))
                            ),
                            RoundedCornerShape(12.dp)
                        ) else Modifier
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(icon, fontSize = 20.sp, color = if (isSelected) Color.White else Color(0xFF95A5A6))
                Text(
                    label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.White else Color(0xFF95A5A6)
                )
            }
        }
    }
}

@Composable
fun HeaderSection(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color(0xFF2C3E50),
    height: Dp = 72.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "YourDietBuddy",
                color = contentColor,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = contentColor
                )
            }
        }
    }
}
