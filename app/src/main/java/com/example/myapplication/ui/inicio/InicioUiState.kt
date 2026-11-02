package com.example.myapplication.ui.inicio

import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.local.entity.Produto

data class InicioUiState(
    val listaDeProdutos: List<Produto> = emptyList(),
    val listaDeCategorias: List<Categoria> = emptyList()
) {
    val produtosOferta: List<Produto>
        get() = listaDeProdutos.filter { !it.desconto.isNullOrBlank() }

    val produtosNormais: List<Produto>
        get() = listaDeProdutos.filter { it.desconto.isNullOrBlank() }
}