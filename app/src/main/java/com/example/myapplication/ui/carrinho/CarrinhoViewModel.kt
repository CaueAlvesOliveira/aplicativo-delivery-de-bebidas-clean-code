package com.example.myapplication.ui.carrinho

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.ItemCarrinho
import com.example.myapplication.data.repository.CarrinhoRepository
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CarrinhoViewModel(
    private val repositoryCarrinho: CarrinhoRepository,
    private val repositoryProduto: ProdutoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CarrinhoUiState())

    val uiState: StateFlow<CarrinhoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repositoryCarrinho.getAllCarrinho().collect { itensCarrinho ->
                _uiState.update { currentState ->
                    currentState.copy(carrinho = itensCarrinho)
                }
            }
        }

        viewModelScope.launch {
            repositoryProduto.getAllProdutos().collect { produtos ->
                _uiState.update { currentState ->
                    currentState.copy(produtos = produtos)
                }
            }
        }
    }

    fun onAlterarQuantidade(produtoId: Int, quantidade: Int) {
        if (quantidade <= 0) {
            onRemoverItem(produtoId)
            return
        }
        viewModelScope.launch {
            repositoryCarrinho.salvarItemCarrinho(ItemCarrinho(produtoId, quantidade))
        }
    }

    fun onRemoverItem(produtoId: Int) {
        viewModelScope.launch {
            repositoryCarrinho.removerItemCarrinho(produtoId)
        }
    }
}