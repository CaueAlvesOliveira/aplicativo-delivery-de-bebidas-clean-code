package com.example.myapplication.ui.formProduto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository

class FormProdutoViewModelFactory(
    private val produtoId: Int,
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormProdutoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormProdutoViewModel(produtoId, produtoRepository, categoriaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}