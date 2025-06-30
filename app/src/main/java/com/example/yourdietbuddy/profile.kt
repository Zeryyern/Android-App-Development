package com.example.yourdietbuddy
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val user = viewModel.currentUser
    Log.d("PROFILE_UI", "User in UI: $user")
    val name = user?.name ?: "Nama Tidak Dikenal"
    val email = user?.email ?: "Email tidak tersedia"
    val logoutDialog = remember { mutableStateOf(false) }
    var showHealthDialog by remember { mutableStateOf(false) }
    var showTargetDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.currentUser?.email?.let { email ->
            delay(100)
            viewModel.refreshUser(context, email)
        }
    }

    if (showHealthDialog) {
        AlertDialog(
            onDismissRequest = { showHealthDialog = false },
            confirmButton = {},
            dismissButton = {},
            text = {
                HealthProfileSection(
                    initialData = mapOf(
                        "Kondisi Medis" to (viewModel.currentUser?.kondisiMedis ?: ""),
                        "Alergi" to (viewModel.currentUser?.alergi ?: ""),
                        "Diet Khusus" to (viewModel.currentUser?.dietKhusus ?: "")
                    ),
                    onSave = {
                        viewModel.updateHealthProfile(
                            context = context,
                            kondisi = it["Kondisi Medis"].orEmpty(),
                            alergi = it["Alergi"].orEmpty(),
                            diet = it["Diet Khusus"].orEmpty()
                        )
                        showHealthDialog = false
                    },
                    onCancel = { showHealthDialog = false }
                )
            }
        )
    }

    if (showTargetDialog) {
        AlertDialog(
            onDismissRequest = { showTargetDialog = false },
            confirmButton = {},
            dismissButton = {},
            text = {
                GoalTargetSection(
                    initialData = mapOf(
                        "Kalori Harian" to (viewModel.currentUser?.goalKalori ?: ""),
                        "Berat Ideal" to (viewModel.currentUser?.goalBeratIdeal ?: ""),
                        "Timeline" to (viewModel.currentUser?.goalTimeline ?: "")
                    ),
                    onSave = {
                        viewModel.updateGoalTarget(
                            context = context,
                            kalori = it["Kalori Harian"].orEmpty(),
                            beratIdeal = it["Berat Ideal"].orEmpty(),
                            timeline = it["Timeline"].orEmpty()
                        )
                        showTargetDialog = false
                    },
                    onCancel = { showTargetDialog = false }
                )
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFE8F4F8), Color(0xFFF0F8FF)),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(bottom = 160.dp)
        ) {

            ProfileHeader(
                name = name,
                email = email,
                viewModel = viewModel,
                onAvatarClick = {
                    Toast.makeText(context, "Fitur ganti foto profil", Toast.LENGTH_SHORT).show()
                }
            )

            InfoCards(viewModel = viewModel)

            MenuSection("🧍 Informasi Pribadi", listOf(
                MenuItemData("💊", "Profil Kesehatan", "Kondisi medis, alergi, diet khusus") {
                    showHealthDialog = true
                },
                MenuItemData("🎯", "Target & Tujuan", "Kalori harian, berat ideal, timeline") {
                    showTargetDialog = true
                }
            ))


            MenuSection("💬 Bantuan & Dukungan", listOf(
                MenuItemData("❓", "Pusat Bantuan", "FAQ, panduan penggunaan") {
                    // buka link pusat bantuan
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ibudanbalita.com/forum/diskusi/Tanya-Jawab-Seputar-Diet"))
                    context.startActivity(intent)
                },
                MenuItemData("ℹ", "Tentang YourDietBuddy", "Versi 2.1.0, syarat & ketentuan") {
                    showAboutDialog = true
                },
                MenuItemData("🚪", "Keluar", "Logout dari akun") {
                    logoutDialog.value = true
                }
            ))
        }

        if (logoutDialog.value) {
            AlertDialog(
                onDismissRequest = { logoutDialog.value = false },
                title = { Text("Keluar dari YourDietBuddy") },
                text = {
                    Text("Anda yakin ingin keluar dari akun Anda? Data yang belum disinkronkan mungkin akan hilang.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            logoutDialog.value = false
                            viewModel.logout()
                            Toast.makeText(context, "Berhasil logout", Toast.LENGTH_SHORT).show()

                            navController.navigate("login") {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Text("Keluar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { logoutDialog.value = false }) {
                        Text("Batal")
                    }
                }
            )
        }
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("Tentang YourDietBuddy") },
                text = {
                    Text("YourDietBuddy versi 2.1.0\n\n" +
                            "Aplikasi ini membantu Anda mengatur diet dan target kesehatan dengan mudah.\n\n" +
                            "Syarat dan Ketentuan berlaku.")
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("Tutup")
                    }
                }
            )
        }

    }
}



@Composable
fun ProfileHeader(name: String, email: String, onAvatarClick: () -> Unit, viewModel: ProfileViewModel) {
    val user = viewModel.currentUser
    val hariAktif = user?.tanggalRegistrasi?.let { millis ->
        val days = ((System.currentTimeMillis() - millis) / (1000 * 60 * 60 * 24)).toInt()
        if (days < 0) 0 else days
    } ?: 0


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(Color(0xFF667EEA), Color(0xFF764BA2))))
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Color(0xFFFF7675), Color(0xFFFD79A8))))
                    .clickable { onAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                Text("JD", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF4ECDC4), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .offset(x = 24.dp, y = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("📷", fontSize = 12.sp)
            }
        }
        Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(email, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
        Spacer(modifier = Modifier.height(8.dp))

        val progress = try {
            val awal = user?.beratAwal?.toFloatOrNull() ?: 0f
            val now = user?.berat?.toFloatOrNull() ?: 0f
            val selisih = awal - now
            when {
                selisih > 0 -> "-%.1f kg".format(selisih)
                selisih < 0 -> "+%.1f kg".format(-selisih)
                else -> "0 kg"
            }
        } catch (e: Exception) {
            "0 kg"
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            StatItem(progress, "Progress")
            StatItem("🔥", "Streak")
            StatItem("$hariAktif", "Hari Aktif")
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Text(label, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
    }
}

@Composable
fun InfoCards(viewModel: ProfileViewModel) {
    val user = viewModel.currentUser
    val berat = user?.berat?.takeIf { it.isNotBlank() }?.plus(" kg") ?: "0 kg"
    val tinggi = user?.tinggi?.takeIf { it.isNotBlank() }?.plus(" cm") ?: "0 cm"
    val bmiRaw = user?.bmi ?: "0.0"

    val usia = user?.tanggalLahir?.let { lahirMillis ->
        val dob = Calendar.getInstance().apply { timeInMillis = lahirMillis }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age -= 1
        }
        "$age th"
    } ?: "?? th"

    val bmiFormatted = bmiRaw.toDoubleOrNull()?.let {
        String.format("%.2f", it)
    } ?: bmiRaw

    val cards = listOf(
        Triple("⚖", berat, "Berat Badan"),
        Triple("📏", tinggi, "Tinggi Badan"),
        Triple("🎂", usia, "Usia"),
        Triple("📊", bmiFormatted, "BMI")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(20.dp)
            .height(270.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(cards.size) { index ->
            InfoCard(
                icon = cards[index].first,
                value = cards[index].second,
                label = cards[index].third
            )
        }
    }
}

fun accentColorFor(label: String): Brush = when (label) {
    "Berat Badan" -> Brush.linearGradient(listOf(Color(0xFF667EEA), Color(0xFF764BA2)))
    "Tinggi Badan" -> Brush.linearGradient(listOf(Color(0xFF4ECDC4), Color(0xFF44A08D)))
    "Usia" -> Brush.linearGradient(listOf(Color(0xFFFF7675), Color(0xFFFD79A8)))
    "BMI" -> Brush.linearGradient(listOf(Color(0xFFFFECD2), Color(0xFFFCB69F)))
    else -> Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
}

@Composable
fun InfoCard(icon: String, value: String, label: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.04f),
                clip = false
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .size(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(accentColorFor(label), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(accentColorFor(label), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF2C3E50))
            Text(label, fontSize = 14.sp, color = Color(0xFF7F8C8D))
        }
    }
}


data class MenuItemData(val icon: String, val title: String, val subtitle: String, val onClick: () -> Unit)

@Composable
fun MenuSection(title: String, items: List<MenuItemData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.04f),
                clip = false
            )
            .background(Color.White, RoundedCornerShape(20.dp))
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color(0xFF2C3E50)
        )

        items.forEachIndexed { index, item ->
            MenuItem(item)
            if (index < items.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    thickness = 1.dp,
                    color = Color(0xFFF8F9FA)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}


fun menuIconColor(title: String): Brush = when (title) {
    "Profil Kesehatan" -> Brush.linearGradient(listOf(Color(0xFFFF7675), Color(0xFFFD79A8)))
    "Target & Tujuan" -> Brush.linearGradient(listOf(Color(0xFF4ECDC4), Color(0xFF44A08D)))
    "Pusat Bantuan" -> Brush.linearGradient(listOf(Color(0xFF89F7FE), Color(0xFF66A6FF)))
    "Tentang YourDietBuddy" -> Brush.linearGradient(listOf(Color(0xFFD299C2), Color(0xFFEFE9D7)))
    "Keluar" -> Brush.linearGradient(listOf(Color(0xFFFF9A9E), Color(0xFFFECFEF)))
    else -> Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
}


@Composable
fun MenuItem(item: MenuItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(menuIconColor(item.title), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(item.icon, color = Color.White, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color(0xFF2C3E50))
            Text(item.subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun HealthProfileSection(
    initialData: Map<String, String>,
    onSave: (Map<String, String>) -> Unit,
    onCancel: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    val formData = remember { mutableStateMapOf<String, String>().apply { putAll(initialData) } }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Profil Kesehatan", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (isEditing) {
            listOf("Kondisi Medis", "Alergi", "Diet Khusus").forEach { field ->
                OutlinedTextField(
                    value = formData[field] ?: "",
                    onValueChange = { formData[field] = it },
                    label = { Text(field) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    formData.clear()
                    formData.putAll(initialData)
                    isEditing = false
                    onCancel()
                }) {
                    Text("Batal")
                }
                Button(onClick = {
                    onSave(formData.toMap())
                    isEditing = false
                }) {
                    Text("Selesai")
                }
            }
        } else {
            listOf("Kondisi Medis", "Alergi", "Diet Khusus").forEach { field ->
                Text("$field: ${formData[field].orEmpty()}", modifier = Modifier.padding(vertical = 6.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }
            }
        }
    }
}

@Composable
fun GoalTargetSection(
    initialData: Map<String, String>,
    onSave: (Map<String, String>) -> Unit,
    onCancel: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    val formData = remember { mutableStateMapOf<String, String>().apply { putAll(initialData) } }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Target & Tujuan", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (isEditing) {
            listOf("Kalori Harian", "Berat Ideal", "Timeline").forEach { field ->
                OutlinedTextField(
                    value = formData[field] ?: "",
                    onValueChange = { formData[field] = it },
                    label = { Text(field) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    formData.clear()
                    formData.putAll(initialData)
                    isEditing = false
                    onCancel()
                }) {
                    Text("Batal")
                }
                Button(onClick = {
                    onSave(formData.toMap())
                    isEditing = false
                }) {
                    Text("Selesai")
                }
            }
        } else {
            listOf("Kalori Harian", "Berat Ideal", "Timeline").forEach { field ->
                Text("$field: ${formData[field].orEmpty()}", modifier = Modifier.padding(vertical = 6.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }
            }
        }
    }
}


