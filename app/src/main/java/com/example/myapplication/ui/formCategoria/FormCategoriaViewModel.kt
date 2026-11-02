package com.example.myapplication.ui.formCategoria

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormCategoriaViewModel(
    private val categoriaId: Int,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FormCategoriaUiState())

    val uiState: StateFlow<FormCategoriaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val categoria = categoriaRepository.buscarCategoriaPorId(categoriaId) ?: return@launch
            _uiState.update {
                it.copy(
                    categoriaEmEdicao = categoria,
                    nome = categoria.nome,
                    icone = categoria.icone,
                    cor = categoria.cor
                )
            }
        }
    }

    fun onNomeChange(valor: String) {
        _uiState.update { it.copy(nome = valor) }
    }
    fun onIconeChange(valor: ImageVector) {
        _uiState.update { it.copy(icone = valor) }
    }
    fun onCorChange(valor: Color) {
        _uiState.update { it.copy(cor = valor) }
    }

    fun onSalvar(aoSalvar: () -> Unit) {
        val state = _uiState.value
        _uiState.update { it.copy(tentouSalvar = true) }

        if (state.erroNome != null) {
            return
        }

        val categoria = Categoria(
            id = state.categoriaEmEdicao?.id ?: 0,
            nome = state.nome.trim(),
            icone = state.icone,
            cor = state.cor
        )

        viewModelScope.launch {
            if (state.editando) {
                categoriaRepository.atualizarCategoria(categoria)
            } else {
                categoriaRepository.inserirCategoria(categoria)
            }
            aoSalvar()
        }
    }
}