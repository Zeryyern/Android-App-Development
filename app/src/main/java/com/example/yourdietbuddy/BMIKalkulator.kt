package com.example.yourdietbuddy
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.yourdietbuddy.ui.theme.*
import com.example.yourdietbuddy.ui.theme.PurpleDark
import com.example.yourdietbuddy.ui.theme.PurpleLight
import com.example.yourdietbuddy.ui.theme.TextPrimary
import com.example.yourdietbuddy.ui.theme.TextSecondary
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun calculateBMI(weight: Float, heightCm: Float): Pair<Float, String> {
    val heightM = heightCm / 100
    val bmi = weight / (heightM * heightM)
    val category = when {
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Normal"
        bmi < 30 -> "Overweight"
        else -> "Obese"
    }
    return bmi to category
}

fun getRecommendations(category: String): List<FoodItem> {
    return when (category) {
        "Underweight" -> listOf(
            FoodItem(
                "Avocado Toast",
                "Toast roti, tambahkan irisan alpukat",
                "Roti Gandum, Alpukat",
                320,
                R.drawable.avocado_toast
            ),
            FoodItem(
                "Banana Oatmeal",
                "Masak oat dengan pisang",
                "Oat, Pisang, Susu",
                290,
                R.drawable.banana_oatmeal
            ),
            FoodItem(
                "Smoothie Bowl",
                "Campur buah dengan yogurt",
                "Strawberry, Pisang, Yogurt",
                270,
                R.drawable.smoothie_bowl
            )
        )

        "Normal" -> listOf(
            FoodItem(
                "Grilled Chicken",
                "Panggang ayam dengan rempah",
                "Ayam, Olive oil, Brokoli",
                300,
                R.drawable.grilled_chicken
            ),
            FoodItem(
                "Fruit Salad",
                "Campur buah segar",
                "Apel, Anggur, Kiwi",
                220,
                R.drawable.fruit_salad
            ),
            FoodItem(
                "Boiled Eggs",
                "Rebus telur 8 menit",
                "Telur, Garam",
                150,
                R.drawable.boiled_eggs
            )
        )

        "Overweight" -> listOf(
            FoodItem(
                "Steamed Veggies",
                "Kukus sayuran hingga matang",
                "Wortel, Brokoli, Kacang Panjang",
                160,
                R.drawable.steamed_veggies
            ),
            FoodItem(
                "Tofu Salad",
                "Campur tofu dan sayur",
                "Tahu, Timun, Selada",
                200,
                R.drawable.tofu_salad
            ),
            FoodItem(
                "Oat Porridge",
                "Rebus oat dengan air",
                "Oat, Garam, Air",
                180,
                R.drawable.oat_porridge
            )
        )

        "Obese" -> listOf(
            FoodItem(
                "Zucchini Soup",
                "Rebus dan haluskan zucchini",
                "Zucchini, Bawang Putih",
                140,
                R.drawable.zucchini_soup
            ),
            FoodItem(
                "Grilled Tofu",
                "Panggang tahu tanpa minyak",
                "Tahu, Lada, Kecap",
                180,
                R.drawable.grilled_tofu
            ),
            FoodItem(
                "Cucumber Salad",
                "Campur mentimun dan tomat",
                "Mentimun, Tomat, Jeruk Nipis",
                120,
                R.drawable.cucumber_salad
            )
        )

        else -> emptyList()
    }
}

fun getRecommendedExercises(category: String): List<ExerciseItem> {
    return when (category) {
        "Underweight" -> listOf(
            ExerciseItem(
                name = "Push-Up",
                instructions = "Dari posisi plank, turunkan dada hingga hampir menyentuh lantai, lalu dorong kembali ke atas.",
                reps = "3×10–12 reps",
                imageRes = R.drawable.push_up,
                benefit = "Membangun kekuatan tubuh bagian atas"
            ),
            ExerciseItem(
                name = "Dumbbell Bent-Over Row",
                instructions = "Tekuk pinggang 45°, tarik siku ke atas sejajar badan, lalu turunkan perlahan.",
                reps = "3×12–16 reps",
                imageRes = R.drawable.bent_over_row,
                benefit = "Menguatkan punggung dan otot lengan"
            ),
            ExerciseItem(
                name = "Plank",
                instructions = "Tahan tubuh dalam posisi lurus menggunakan lengan bawah dan ujung kaki.",
                reps = "3×30–45 detik",
                imageRes = R.drawable.plank,
                benefit = "Menguatkan core dan meningkatkan postur"
            )
        )

        "Normal" -> listOf(
            ExerciseItem(
                name = "High Knees",
                instructions = "Angkat lutut secara bergantian setinggi pinggang sambil bergerak di tempat.",
                reps = "3×30 detik",
                imageRes = R.drawable.high_knees,
                benefit = "Meningkatkan stamina dan membakar kalori"
            ),
            ExerciseItem(
                name = "Crunches",
                instructions = "Berbaring, angkat bahu sambil menarik otot perut, lalu kembali turun perlahan.",
                reps = "3×15–20 reps",
                imageRes = R.drawable.crunches,
                benefit = "Melatih dan mengencangkan otot perut"
            ),
            ExerciseItem(
                name = "Forward Leg Swings",
                instructions = "Berdiri, pegang dinding, ayunkan satu kaki ke depan dan belakang, lalu ganti kaki.",
                reps = "3×15 swings per sisi",
                imageRes = R.drawable.leg_swings,
                benefit = "Meningkatkan fleksibilitas dan keseimbangan pinggul"
            )
        )

        "Overweight" -> listOf(
            ExerciseItem(
                name = "Run In Place",
                instructions = "Berlari kecil di tempat dengan gerakan tangan aktif.",
                reps = "3×30 detik",
                imageRes = R.drawable.run_in_place,
                benefit = "Meningkatkan detak jantung tanpa tekanan berlebih"
            ),
            ExerciseItem(
                name = "Crunches",
                instructions = "Latihan perut klasik untuk mengaktifkan otot core dengan tekanan rendah.",
                reps = "3×12–15 reps",
                imageRes = R.drawable.crunches,
                benefit = "Menguatkan otot perut tanpa stres tinggi"
            ),
            ExerciseItem(
                name = "Forward Leg Swings",
                instructions = "Gerakan ringan untuk memanaskan sendi pinggul dan paha.",
                reps = "3×15 swings per sisi",
                imageRes = R.drawable.leg_swings,
                benefit = "Menambah mobilitas pinggul dan paha"
            )
        )

        "Obese" -> listOf(
            ExerciseItem(
                name = "Forward Leg Swings",
                instructions = "Gerakan ringan untuk memulai mobilitas tubuh bagian bawah.",
                reps = "3×15 swings per sisi",
                imageRes = R.drawable.leg_swings,
                benefit = "Membantu mobilisasi ringan dan aman"
            ),
            ExerciseItem(
                name = "Run In Place (Modifikasi)",
                instructions = "Lari ringan di tempat dengan langkah lebih pelan dan stabil.",
                reps = "3×20 detik",
                imageRes = R.drawable.run_in_place,
                benefit = "Kardio aman untuk pemula dengan berat badan tinggi"
            ),
            ExerciseItem(
                name = "Knee & Elbow Press-Up",
                instructions = "Mulai dari posisi lutut dan siku menyentuh lantai, dorong pinggul ke atas seperti membentuk huruf V, lalu kembali ke posisi semula.",
                reps = "3×45–60 detik",
                imageRes = R.drawable.knee_elbow_pressup,
                benefit = "Melatih core dan punggung bawah dengan tekanan minimal"
            )

        )

        else -> emptyList()
    }
}

@Composable
fun BMICalculatorScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    selectedNav: MutableState<String>,
    onBMIResult: (String, List<FoodItem>) -> Unit
) {
    var heightInput by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var showSaveDialog by remember { mutableStateOf(false) }
    var calculatedBMI by remember { mutableFloatStateOf(0f) }
    var bmiCategory by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.Transparent
    ) { innerPadding ->
        // UI utama
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PurpleLight, PurpleDark)
                    )
                )
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBg)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    label = { Text("Height (cm)", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleDark,
                        cursorColor = PurpleDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        unfocusedContainerColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (kg)", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleDark,
                        cursorColor = PurpleDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        unfocusedContainerColor = Color.White
                    )
                )

                Button(
                    onClick = {
                        val height = heightInput.toFloatOrNull()
                        val weight = weightInput.toFloatOrNull()
                        if (height != null && weight != null) {
                            val (bmi, category) = calculateBMI(weight, height)
                            resultText = "Your BMI: %.2f (%s)".format(bmi, category)
                            calculatedBMI = bmi
                            bmiCategory = category
                            showSaveDialog = true
                        } else {
                            resultText = "Please enter valid numbers."
                        }
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4ECDC4),
                        contentColor = ButtonText
                    )
                ) {
                    Text("Calculate BMI", fontWeight = FontWeight.Bold)
                }

                if (resultText.isNotEmpty() && showSaveDialog) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = resultText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Tombol Kembali - di kiri
                        Button(
                            onClick = {
                                onBMIResult(bmiCategory, getRecommendations(bmiCategory))
                                navController.navigate("dashboard")
                            },
                            modifier = Modifier.widthIn(min = 120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Kembali")
                        }

                        // Tombol Simpan - di kanan
                        Button(
                            onClick = {
                                if (weightInput.isBlank() || heightInput.isBlank()) {
                                    Toast.makeText(context, "Berat dan tinggi harus diisi!", Toast.LENGTH_SHORT).show()
                                } else {
                                scope.launch {
                                    profileViewModel.updateBMI(
                                        context = context,
                                        weightInput,
                                        heightInput,
                                        bmi = calculatedBMI.toString()
                                    )

                                    delay(200) // beri waktu untuk Room menyimpan
                                    profileViewModel.refreshUser(context, profileViewModel.currentUser?.email ?: "")

                                    onBMIResult(bmiCategory, getRecommendations(bmiCategory))
                                    selectedNav.value = "🏡"

                                    navController.navigate("dashboard") {
                                        popUpTo("bmi") { inclusive = true }
                                    }
                                }
                                }
                            },
                            modifier = Modifier.widthIn(min = 120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4ECDC4),
                                contentColor = ButtonText
                            )
                        ) {
                            Text("Simpan")
                        }
                    }
                }
            }
        }
    }
}
}