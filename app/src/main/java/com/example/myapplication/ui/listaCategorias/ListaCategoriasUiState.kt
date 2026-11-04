package com.example.myapplication.ui.listaCategorias

import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.local.entity.Produto

data class ListaCategoriasUiState(
    val categorias: List<Categoria> = emptyList(),
    val produtos: List<Produto> = emptyList(),
    val categoriaParaExcluir: Categoria? = null
) {
    fun quantidadeProdutos(categoriaId: Int): Int {
        return produtos.count { it.categoriaId == categoriaId }
    }
}