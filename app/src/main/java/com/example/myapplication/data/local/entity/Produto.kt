package com.example.myapplication.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val volume: String,
    val estabelecimento: String,
    val preco: Double,
    val imagem: Int,
    val desconto: String? = null,
    val descricao: String,
    val categoriaId: Int
)