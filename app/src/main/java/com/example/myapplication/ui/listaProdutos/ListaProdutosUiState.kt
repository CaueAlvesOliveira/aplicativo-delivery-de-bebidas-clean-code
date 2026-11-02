package com.example.myapplication.ui.listaProdutos

import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.local.entity.Produto

data class ListaProdutosUiState(
    val produtos: List<Produto> = emptyList(),
    val categorias: List<Categoria> = emptyList(),
    val produtoParaExcluir: Produto? = null
) {
    fun nomeCategoria(categoriaId: Int): String? {
        return categorias.find { it.id == categoriaId }?.nome
    }
}