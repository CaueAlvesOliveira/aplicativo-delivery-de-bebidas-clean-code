package com.example.myapplication.ui.carrinho

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CarrinhoRepository
import com.example.myapplication.data.repository.ProdutoRepository

class CarrinhoViewModelFactory(
    private val carrinhoRepository: CarrinhoRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarrinhoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarrinhoViewModel(carrinhoRepository, produtoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}