package com.example.plantguideapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val longDescription: String,
    val category: String,
    val imageResName: String,
    val isFavorite: Boolean = false // новое поле
)
