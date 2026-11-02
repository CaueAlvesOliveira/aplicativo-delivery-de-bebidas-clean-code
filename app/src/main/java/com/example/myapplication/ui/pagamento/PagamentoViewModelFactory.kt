package com.example.myapplication.ui.pagamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CarrinhoRepository
import com.example.myapplication.data.repository.ProdutoRepository

class PagamentoViewModelFactory(
    private val carrinhoRepository: CarrinhoRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PagamentoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PagamentoViewModel(carrinhoRepository, produtoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}