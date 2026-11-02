package com.example.myapplication.ui.produto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.ItemCarrinho
import com.example.myapplication.data.repository.CarrinhoRepository
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProdutoViewModel(
    private val produtoId: Int,
    private val produtoRepository: ProdutoRepository,
    private val carrinhoRepository: CarrinhoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProdutoUiState())

    val uiState: StateFlow<ProdutoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val produto = produtoRepository.buscarProdutoPorId(produtoId)
            _uiState.update { it.copy(produto = produto, carregando = false) }
        }
    }

    fun onQuantidadeChange(novaQuantidade: Int) {
        _uiState.update { it.copy(quantidade = novaQuantidade) }
    }

    fun onAdicionarAoCarrinho(quantidade: Int) {
        viewModelScope.launch {
            carrinhoRepository.adicionarAoCarrinho(produtoId, quantidade)
        }
    }
}