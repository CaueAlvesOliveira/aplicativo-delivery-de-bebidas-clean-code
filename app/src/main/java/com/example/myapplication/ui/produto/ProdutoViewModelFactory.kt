package com.example.myapplication.ui.produto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CarrinhoRepository
import com.example.myapplication.data.repository.ProdutoRepository

class ProdutoViewModelFactory(
    private val produtoId: Int,
    private val produtoRepository: ProdutoRepository,
    private val carrinhoRepository: CarrinhoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProdutoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProdutoViewModel(produtoId, produtoRepository, carrinhoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}