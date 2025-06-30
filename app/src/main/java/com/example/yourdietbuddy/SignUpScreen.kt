package com.example.yourdietbuddy

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Locale
import android.app.DatePickerDialog
import java.util.*


@Composable
fun SignUpScreen(navController: NavHostController, profileViewModel: ProfileViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var birthdateMillis by remember { mutableLongStateOf(0L) }
    var birthdateText by remember { mutableStateOf("Belum dipilih") }
    var isLoading by remember { mutableStateOf(false) }

    val logoScale by rememberInfiniteTransition(label = "signup-float").animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(listOf(Color(0xFF667EEA), Color(0xFF764BA2)))
            )
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(logoScale)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFF6B6B), Color(0xFF4ECDC4))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("Daftar Akun", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Lengkap") },
                    placeholder = { Text("Masukkan nama Anda") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4ECDC4),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF4ECDC4),
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedLabelColor = Color.Gray,
                        disabledTextColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("Masukkan email Anda") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4ECDC4),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF4ECDC4),
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedLabelColor = Color.Gray
                    )
                )


                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    placeholder = { Text("Masukkan password") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4ECDC4),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF4ECDC4),
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        showDatePicker(context) { selectedMillis ->
                            birthdateMillis = selectedMillis
                            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            birthdateText = formatter.format(Date(selectedMillis))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ECDC4))
                ) {
                    Text("Tanggal Lahir: $birthdateText")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            name.isBlank() || email.isBlank() || password.isBlank() || birthdateMillis == 0L -> {
                                showToast(context, "Semua field harus diisi!")
                            }
                            !isValidEmail(email) -> {
                                showToast(context, "Format email tidak valid!")
                            }
                            password.length < 6 -> {
                                showToast(context, "Password minimal 6 karakter!")
                            }
                            else -> {
                                isLoading = true
                                scope.launch {
                                    profileViewModel.register(context, name, email, password, birthdateMillis) { success ->
                                        isLoading = false
                                        if (success) {
                                            showToast(context, "Registrasi berhasil!")
                                            navController.navigate("login") {
                                                popUpTo("signup") { inclusive = true }
                                            }
                                        } else {
                                            showToast(context, "Email sudah terdaftar")
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLoading) Color.Gray else Color(0xFF4ECDC4)
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Daftar", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { navController.navigate("login") }) {
                    Text("Sudah punya akun? Masuk di sini", color = Color(0xFF4ECDC4))
                }
            }
        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (Long) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            val selectedCal = Calendar.getInstance()
            selectedCal.set(y, m, d)
            onDateSelected(selectedCal.timeInMillis)
        },
        year,
        month,
        day
    )
    datePickerDialog.show()
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
