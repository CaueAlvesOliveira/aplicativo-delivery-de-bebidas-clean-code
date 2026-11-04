package com.example.myapplication.ui.produtosPorCategoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository

class ProdutosPorCategoriaViewModelFactory(
    private val categoriaId: Int,
    private val categoriaRepository: CategoriaRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProdutosPorCategoriaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProdutosPorCategoriaViewModel(categoriaId, categoriaRepository, produtoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}