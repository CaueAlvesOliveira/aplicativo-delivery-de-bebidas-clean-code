package com.example.myapplication.ui.listaProdutos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.Produto
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListaProdutosViewModel(
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListaProdutosUiState())

    val uiState: StateFlow<ListaProdutosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            produtoRepository.getAllProdutos().collect { produtos ->
                _uiState.update { currentState ->
                    currentState.copy(produtos = produtos)
                }
            }
        }

        viewModelScope.launch {
            categoriaRepository.getAllCategorias().collect { categorias ->
                _uiState.update { currentState ->
                    currentState.copy(categorias = categorias)
                }
            }
        }
    }

    fun onExcluirClick(produto: Produto) {
        _uiState.update { it.copy(produtoParaExcluir = produto) }
    }

    fun onCancelarExclusao() {
        _uiState.update { it.copy(produtoParaExcluir = null) }
    }

    fun onConfirmarExclusao() {
        val produto = _uiState.value.produtoParaExcluir ?: return
        viewModelScope.launch {
            produtoRepository.deletarProduto(produto)
            _uiState.update { it.copy(produtoParaExcluir = null) }
        }
    }
}