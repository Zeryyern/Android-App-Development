package com.example.yourdietbuddy

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class ProfileViewModel : ViewModel() {
    var currentUser by mutableStateOf<UserEntity?>(null)
        private set

    // Fungsi untuk register user baru
    fun register(context: Context, name: String, email: String, password: String, tanggalLahir: Long, onResult: (Boolean) -> Unit) {
        val dao = AppDatabase.getDatabase(context).userDao()
        viewModelScope.launch {
            val exists = dao.getUserByEmail(email)
            if (exists == null) {
                dao.insert(UserEntity(name = name, email = email, password = password, tanggalLahir = tanggalLahir))
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    // Fungsi login
    fun login(context: Context, email: String, password: String, onResult: (Boolean) -> Unit) {
        val dao = AppDatabase.getDatabase(context).userDao()
        viewModelScope.launch {
            val user = dao.login(email, password)
            if (user != null) {
                currentUser = user
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    // Update BMI di database
    fun updateBMI(context: Context, weight: String, height: String, bmi: String) {
        val dao = AppDatabase.getDatabase(context).userDao()
        viewModelScope.launch {
            currentUser?.let {
                val updated = it.copy(
                    berat = weight,
                    tinggi = height,
                    bmi = bmi,
                    beratAwal = if (it.beratAwal.isBlank()) weight else it.beratAwal
                )
                dao.update(updated)
                currentUser = dao.getUserByEmail(it.email)
            }
        }
    }

    // Update goal (target & timeline diet)
    fun updateGoalTarget(context: Context, kalori: String, beratIdeal: String, timeline: String) {
        val dao = AppDatabase.getDatabase(context).userDao()
        viewModelScope.launch {
            currentUser?.let {
                val updated = it.copy(
                    goalKalori = kalori,
                    goalBeratIdeal = beratIdeal,
                    goalTimeline = timeline
                )
                dao.update(updated)
                currentUser = dao.getUserByEmail(it.email)
            }
        }
    }

    fun logout() {
        currentUser = null
    }

    fun refreshUser(context: Context, email: String) {
        val dao = AppDatabase.getDatabase(context).userDao()
        viewModelScope.launch {
            val newUser = dao.getUserByEmail(email)
            Log.d("REFRESH_USER", "Fetched user: $newUser")
            currentUser = newUser
        }
    }

    // Update data kesehatan
    fun updateHealthProfile(context: Context, kondisi: String, alergi: String, diet: String) {
        val dao = AppDatabase.getDatabase(context).userDao()
        viewModelScope.launch {
            currentUser?.let {
                val updated = it.copy(
                    kondisiMedis = kondisi,
                    alergi = alergi,
                    dietKhusus = diet
                )
                dao.update(updated)
                currentUser = dao.getUserByEmail(it.email)
            }
        }
    }
}
