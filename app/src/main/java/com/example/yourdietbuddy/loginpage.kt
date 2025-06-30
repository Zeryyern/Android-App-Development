package com.example.yourdietbuddy

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.Offset
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, profileViewModel: ProfileViewModel, selectedNav: MutableState<String>) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    var enableFloating by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
        delay(1000)
        enableFloating = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        FloatingCircle(Modifier.offset(40.dp, 100.dp).size(80.dp), floatY)
        FloatingCircle(Modifier.offset(280.dp, 150.dp).size(120.dp), floatY * 0.7f)
        FloatingCircle(Modifier.offset(80.dp, 500.dp).size(60.dp), floatY * 1.3f)

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 3 },
                animationSpec = tween(durationMillis = 600)
            ) + fadeIn(animationSpec = tween(durationMillis = 600)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                modifier = Modifier.padding(24.dp).fillMaxWidth().wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(30.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(80.dp).scale(logoScale).clip(RoundedCornerShape(20.dp)).background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFF6B6B), Color(0xFF4ECDC4))
                            )
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo YourDietBuddy",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("YourDietBuddy", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3748))
                    Text("Pantau Diet Sehat Anda", fontSize = 14.sp, color = Color(0xFF718096), modifier = Modifier.padding(bottom = 40.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("Masukkan email Anda") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4ECDC4),
                            focusedLabelColor = Color(0xFF4ECDC4),
                            unfocusedContainerColor = Color(0xFFF7FAFC),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        placeholder = { Text("Masukkan password Anda") },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null,
                                    tint = Color(0xFF718096)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4ECDC4),
                            focusedLabelColor = Color(0xFF4ECDC4),
                            unfocusedContainerColor = Color(0xFFF7FAFC),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            showToast(context, "Demo: Fitur lupa password akan mengirim email reset ke alamat yang terdaftar.")
                        }) {
                            Text("Lupa Password?", color = Color(0xFF4ECDC4), fontSize = 14.sp)
                        }
                    }

                    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                    fun isValidEmail(email: String) = email.matches(emailRegex)
                    fun isValidPassword(password: String) = password.length >= 6
                    val scope = rememberCoroutineScope()

                    Button(
                        onClick = {
                            when {
                                email.isBlank() || password.isBlank() -> {
                                    showToast(context, "Email dan password harus diisi!")
                                }
                                !isValidEmail(email) -> {
                                    showToast(context, "Format email tidak valid!")
                                }
                                !isValidPassword(password) -> {
                                    showToast(context, "Password minimal 6 karakter!")
                                }
                                else -> {
                                    isLoading = true
                                    scope.launch {
                                        profileViewModel.login(context, email, password) { success ->
                                            isLoading = false
                                            if (success) {
                                                selectedNav.value = "📊"
                                                navController.navigate("bmi") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                                showToast(context, "Login berhasil! Selamat datang!")
                                            } else {
                                                showToast(context, "Email atau password salah!")
                                            }
                                        }
                                    }
                                }
                            }
                        },

                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLoading) Color(0xFFA0AEC0) else Color(0xFF4ECDC4)
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sedang Masuk...", color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        } else {
                            Text("Masuk", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                        Text("atau masuk dengan", modifier = Modifier.padding(horizontal = 16.dp), fontSize = 14.sp, color = Color(0xFFA0AEC0))
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SocialLoginButton("🌐", Modifier.weight(1f)) {
                            showToast(context, "Demo: Login dengan Google")
                            coroutineScope.launch {
                                delay(1000)
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                        SocialLoginButton("📘", Modifier.weight(1f)) {
                            showToast(context, "Demo: Login dengan Facebook")
                            coroutineScope.launch {
                                delay(1000)
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Belum punya akun? ", fontSize = 14.sp, color = Color(0xFF718096))
                        TextButton(onClick = { navController.navigate("signup") }, contentPadding = PaddingValues(0.dp)) {
                            Text("Daftar Sekarang", fontSize = 14.sp, color = Color(0xFF4ECDC4), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingCircle(modifier: Modifier = Modifier, offsetY: Float = 0f) {
    Box(
        modifier = modifier
            .offset(y = offsetY.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.1f))
    )
}

@Composable
fun SocialLoginButton(icon: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(48.dp)
            .clickable {
                isPressed = true
                onClick()
            }
            .scale(if (isPressed) 0.95f else 1f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
