package com.example.myapplication.model

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey


@Entity(
    tableName = "carrinho",
    foreignKeys = [
        ForeignKey(
            entity = Produto::class,
            parentColumns = ["id"],
            childColumns = ["produtoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItemCarrinho(
    @PrimaryKey
    val produtoId: Int,
    val quantidade: Int
)
