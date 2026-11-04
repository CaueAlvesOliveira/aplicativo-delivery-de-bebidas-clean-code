package com.example.myapplication.ui.listaProdutos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository

class ListaProdutosViewModelFactory(
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaProdutosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListaProdutosViewModel(produtoRepository, categoriaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}