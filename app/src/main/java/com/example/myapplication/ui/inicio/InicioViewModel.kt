package com.example.myapplication.ui.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CarrinhoRepository
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository
import com.example.myapplication.data.local.popularBancoSeVazio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InicioViewModel(
    private val repositoryProduto: ProdutoRepository,
    private val repositoryCategoria: CategoriaRepository,
    private val repositoryCarrinho: CarrinhoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(InicioUiState())

    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            popularBancoSeVazio(repositoryCategoria, repositoryProduto)
        }

        viewModelScope.launch {
            repositoryProduto.getAllProdutos().collect { produtos ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeProdutos = produtos)
                }
            }
        }

        viewModelScope.launch {
            repositoryCategoria.getAllCategorias().collect { categorias ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeCategorias = categorias)
                }
            }
        }
    }

    fun onAdicionarAoCarrinho(produtoId: Int, quantidade: Int = 1) {
        viewModelScope.launch {
            repositoryCarrinho.adicionarAoCarrinho(produtoId, quantidade)
        }
    }
}