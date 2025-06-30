package com.example.yourdietbuddy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val email: String,
    val password: String,
    val tanggalRegistrasi: Long = System.currentTimeMillis(),
    val tanggalLahir: Long = 0L,


    // Data tubuh & kesehatan
    val berat: String = "",
    val beratAwal: String = "",
    val tinggi: String = "",
    val bmi: String = "",

    // Data target/timeline diet
    val goalKalori: String = "",
    val goalBeratIdeal: String = "",
    val goalTimeline: String = "",

    // Info medis pribadi
    val kondisiMedis: String = "",
    val alergi: String = "",
    val dietKhusus: String = ""
)
