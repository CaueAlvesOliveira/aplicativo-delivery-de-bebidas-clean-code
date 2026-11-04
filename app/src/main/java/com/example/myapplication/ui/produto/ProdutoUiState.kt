package com.example.myapplication.ui.produto

import com.example.myapplication.data.local.entity.Produto

data class ProdutoUiState(
    val produto: Produto? = null,
    val carregando: Boolean = true,
    val quantidade: Int = 1
)