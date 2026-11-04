package com.example.myapplication.ui.listaCategorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository

class ListaCategoriasViewModelFactory(
    private val categoriaRepository: CategoriaRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaCategoriasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListaCategoriasViewModel(categoriaRepository, produtoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}