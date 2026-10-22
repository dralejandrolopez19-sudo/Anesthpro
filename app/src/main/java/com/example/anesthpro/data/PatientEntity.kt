package com.example.anesthpro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val age: Int,
    val weightKg: Double,
    val heightCm: Double,
    val surgeryType: String,
    val asaClass: String,
    val allergies: String,
    val notes: String,
    val createdAt: Long = System.currentTimeMillis()
)
