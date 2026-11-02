package com.example.myapplication.ui.produtosPorCategoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProdutosPorCategoriaViewModel(
    private val categoriaId: Int,
    private val categoriaRepository: CategoriaRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProdutosPorCategoriaUiState())

    val uiState: StateFlow<ProdutosPorCategoriaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val categoria = categoriaRepository.buscarCategoriaPorId(categoriaId)
            _uiState.update { it.copy(categoria = categoria, carregando = false) }
        }

        viewModelScope.launch {
            produtoRepository.getAllProdutos().collect { produtos ->
                _uiState.update { currentState ->
                    currentState.copy(produtos = produtos.filter { it.categoriaId == categoriaId })
                }
            }
        }
    }
}