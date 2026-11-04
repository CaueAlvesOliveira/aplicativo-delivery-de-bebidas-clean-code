package com.example.myapplication.ui.listaCategorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListaCategoriasViewModel(
    private val categoriaRepository: CategoriaRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListaCategoriasUiState())

    val uiState: StateFlow<ListaCategoriasUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoriaRepository.getAllCategorias().collect { categorias ->
                _uiState.update { currentState ->
                    currentState.copy(categorias = categorias)
                }
            }
        }

        viewModelScope.launch {
            produtoRepository.getAllProdutos().collect { produtos ->
                _uiState.update { currentState ->
                    currentState.copy(produtos = produtos)
                }
            }
        }
    }

    fun onExcluirClick(categoria: Categoria) {
        _uiState.update { it.copy(categoriaParaExcluir = categoria) }
    }

    fun onCancelarExclusao() {
        _uiState.update { it.copy(categoriaParaExcluir = null) }
    }

    fun onConfirmarExclusao() {
        val categoria = _uiState.value.categoriaParaExcluir ?: return
        viewModelScope.launch {
            categoriaRepository.deletarCategoria(categoria)
            _uiState.update { it.copy(categoriaParaExcluir = null) }
        }
    }
}