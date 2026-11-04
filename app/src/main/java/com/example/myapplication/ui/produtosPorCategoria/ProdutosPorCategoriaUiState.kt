package com.example.myapplication.ui.produtosPorCategoria

import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.local.entity.Produto

data class ProdutosPorCategoriaUiState(
    val categoria: Categoria? = null,
    val produtos: List<Produto> = emptyList(),
    val carregando: Boolean = true
)