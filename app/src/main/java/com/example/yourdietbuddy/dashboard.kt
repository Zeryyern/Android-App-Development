package com.example.yourdietbuddy

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgGradient = Brush.verticalGradient(listOf(Color(0xFFF5F7FA), Color(0xFFC3CFE2)))
private val PrimaryText = Color(0xFF2D3748)
private val SecondaryText = Color(0xFF4A5568)
private val AccentBlue = Color(0xFF3182CE)
private val AccentBlueLight = Color(0xFF63B3ED)
private val AccentGreen = Color(0xFF38A169)
private val AccentYellow = Color(0xFFD69E2E)
private val AccentRed = Color(0xFFE53E3E)
private val CardBg = Color.White
private val CardBgLight = Color(0xFFF8FAFC)
private val DialogBg = Color(0xFFE2E8F0)

@Composable
fun DashboardScreen(
    recommendedFoods: List<FoodItem>,
    recommendedExercises: List<ExerciseItem>,
    bmiCategory: String
) {
    var meals by remember { mutableStateOf(emptyList<Meal>()) }
    var selectedFood: FoodItem? by remember { mutableStateOf(null) }
    var selectedExercise: ExerciseItem? by remember { mutableStateOf(null) }
    var showFoodPicker by remember { mutableStateOf(false) }

    val targetCalories = when (bmiCategory.lowercase()) {
        "underweight" -> 2500
        "normal"      -> 2000
        "overweight"  -> 1600
        else           -> 2000
    }

    Scaffold(containerColor = Color.Transparent) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgGradient)
                .padding(inner)
        ) {
            when {
                selectedExercise != null -> ExerciseDetailView(selectedExercise!!) { selectedExercise = null }
                selectedFood     != null -> FoodDetailView(selectedFood!!)       { selectedFood = null     }
                else -> DashboardContent(
                    meals               = meals,
                    onAddMeal           = { showFoodPicker = true },
                    onDeleteMeal        = { meal -> meals -= meal },
                    onFoodClick         = { selectedFood = it },
                    onExerciseClick     = { selectedExercise = it },
                    foodList            = recommendedFoods,
                    exerciseList        = recommendedExercises,
                    targetCalories      = targetCalories
                )
            }

            if (showFoodPicker) {
                SelectFoodDialog(
                    foodList = recommendedFoods,
                    onDismiss = { showFoodPicker = false },
                    onSelect = { food ->
                        meals += Meal(type = food.name, calories = food.calories)
                        showFoodPicker = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DashboardContent(
    meals: List<Meal>,
    onAddMeal: () -> Unit,
    onDeleteMeal: (Meal) -> Unit,
    onFoodClick: (FoodItem) -> Unit,
    onExerciseClick: (ExerciseItem) -> Unit,
    foodList: List<FoodItem>,
    exerciseList: List<ExerciseItem>,
    targetCalories: Int
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.height(16.dp))
            HeaderSection()
            CalorieProgressSection(meals, targetCalories)
            Spacer(Modifier.height(20.dp))
            RecommendedFoods(foodList, onFoodClick)
            Spacer(Modifier.height(12.dp))
            RecommendedExercises(exerciseList, onExerciseClick)
            Spacer(Modifier.height(24.dp))
            FoodListHeader(onAddMeal)
            Spacer(Modifier.height(8.dp))
        }

        items(meals) { MealCard(it, onDeleteMeal) }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun HeaderSection() {
    Text("Progress Hari Ini", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = PrimaryText)
    Text("14 Juni 2025", fontSize = 16.sp, color = SecondaryText)
}

@Composable
private fun CalorieProgressSection(meals: List<Meal>, targetCalories: Int) {
    val total = meals.sumOf { it.calories }
    val progress = (total.toFloat() / targetCalories).coerceIn(0f, 1f)
    val remaining = (targetCalories - total).coerceAtLeast(0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(CardBg),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$total", fontWeight = FontWeight.Bold, fontSize = 36.sp, color = AccentBlue)
            Text("dari $targetCalories kal", fontSize = 16.sp, color = SecondaryText)
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = AccentBlue,
                trackColor = DialogBg
            )
            Spacer(Modifier.height(12.dp))
            Text("$remaining kal tersisa", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = SecondaryText)
        }
    }
}

@Composable
private fun SelectFoodDialog(
    foodList: List<FoodItem>,
    onDismiss: () -> Unit,
    onSelect: (FoodItem) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DialogBg,
        title = {
            Text("Pilih Makanan", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryText)
        },
        text = {
            LazyColumn(Modifier.height(360.dp)) {
                items(foodList) { food ->
                    FoodPickItem(food) { onSelect(food) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Tutup", color = AccentBlueLight) }
        }
    )
}

@Composable
private fun FoodPickItem(food: FoodItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(CardBgLight),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(food.imageRes),
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(1.dp, DialogBg, CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(food.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = PrimaryText)
                Text("${food.calories} kal", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = AccentBlue)
            }
        }
    }
}

@Composable
private fun RecommendedFoods(foodList: List<FoodItem>, onFoodClick: (FoodItem) -> Unit) {
    SectionTitle("Rekomendasi Makanan")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(foodList) { FoodCard(it, onFoodClick) }
    }
}

@Composable
private fun FoodCard(food: FoodItem, onClick: (FoodItem) -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(food) },
        colors = CardDefaults.cardColors(CardBg),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(food.imageRes),
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(Modifier.fillMaxSize().background(Color.Black.copy(0.3f)))
            Text(
                food.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }
    }
}

@Composable
private fun RecommendedExercises(exerciseList: List<ExerciseItem>, onExerciseClick: (ExerciseItem) -> Unit) {
    SectionTitle("Rekomendasi Olahraga")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(exerciseList) { ExerciseCard(it, onExerciseClick) }
    }
}

@Composable
private fun ExerciseCard(exercise: ExerciseItem, onClick: (ExerciseItem) -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(exercise) },
        colors = CardDefaults.cardColors(CardBg),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(exercise.imageRes),
                contentDescription = exercise.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(Modifier.fillMaxSize().background(Color.Black.copy(0.3f)))
            Text(
                exercise.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
            )
        }
    }
}

@Composable
private fun FoodDetailView(food: FoodItem, onBack: () -> Unit) {
    DetailScaffold(title = food.name, imageRes = food.imageRes, onBack = onBack) {
        Text("Instruksi: ${food.instructions}", fontSize = 16.sp, color = PrimaryText)
        Text("Bahan: ${food.ingredients}",   fontSize = 16.sp, color = PrimaryText)
        Text("Kalori: ${food.calories} kal", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = PrimaryText)
    }
}

@Composable
private fun ExerciseDetailView(exercise: ExerciseItem, onBack: () -> Unit) {
    DetailScaffold(title = exercise.name, imageRes = exercise.imageRes, onBack = onBack) {
        Text("Instruksi: ${exercise.instructions}", fontSize = 16.sp, color = PrimaryText)
        Text("Repetisi: ${exercise.reps}",      fontSize = 16.sp, color = PrimaryText)
        Text("Manfaat: ${exercise.benefit}",    fontSize = 16.sp, color = PrimaryText)
    }
}

@Composable
private fun DetailScaffold(title: String, imageRes: Int, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = PrimaryText)
        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(imageRes),
            contentDescription = title,
            modifier = Modifier.height(200.dp).clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))
        content()
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Kembali") }
    }
}

@Composable
private fun FoodListHeader(onAddMeal: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Makanan Hari Ini", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryText)
        Icon(Icons.Default.Add, contentDescription = "Tambah", tint = AccentBlue, modifier = Modifier.clickable { onAddMeal() })
    }
}

@Composable
private fun MealCard(meal: Meal, onDelete: (Meal) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(CardBg),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Fastfood, contentDescription = null, tint = DialogBg, modifier = Modifier.size(40.dp).clip(CircleShape).background(DialogBg))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(meal.type, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryText)
                if (meal.description.isNotBlank()) Text(meal.description, fontSize = 13.sp, color = SecondaryText)
            }
            Column(horizontalAlignment = Alignment.End) {
                val kcalColor = when {
                    meal.calories < 200 -> AccentGreen
                    meal.calories < 300 -> AccentYellow
                    else                -> AccentRed
                }
                Text("${meal.calories} kal", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = kcalColor)
                IconButton(onClick = { onDelete(meal) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = SecondaryText)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryText, modifier = Modifier.fillMaxWidth())
}

data class Meal(
    val time: String = "",
    val type: String,
    val description: String = "",
    val calories: Int
)

data class FoodItem(
    val name: String,
    val instructions: String = "",
    val ingredients: String = "",
    val calories: Int,
    val imageRes: Int
)

data class ExerciseItem(
    val name: String,
    val instructions: String,
    val reps: String,
    val imageRes: Int,
    val benefit: String
)

