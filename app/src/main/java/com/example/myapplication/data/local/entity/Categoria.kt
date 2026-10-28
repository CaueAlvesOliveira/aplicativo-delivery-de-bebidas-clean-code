package com.example.myapplication.data.local.entity

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val icone: ImageVector,
    val cor: Color
)