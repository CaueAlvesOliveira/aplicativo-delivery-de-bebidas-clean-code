package com.example.myapplication.ui.formProduto

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
import java.util.Locale

class FormProdutoViewModel(
    private val produtoId: Int,
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FormProdutoUiState())

    val uiState: StateFlow<FormProdutoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoriaRepository.getAllCategorias().collect { categorias ->
                _uiState.update { currentState ->
                    currentState.copy(
                        categorias = categorias,
                        categoriaId = if (currentState.categoriaId == -1) {
                            categorias.firstOrNull()?.id ?: -1
                        } else {
                            currentState.categoriaId
                        }
                    )
                }
            }
        }

        viewModelScope.launch {
            val produto = produtoRepository.buscarProdutoPorId(produtoId) ?: return@launch
            _uiState.update {
                it.copy(
                    produtoEmEdicao = produto,
                    nome = produto.nome,
                    volume = produto.volume,
                    estabelecimento = produto.estabelecimento,
                    precoTexto = String.format(Locale.forLanguageTag("pt-BR"), "%.2f", produto.preco),
                    descontoTexto = produto.desconto ?: "",
                    descricao = produto.descricao,
                    imagem = produto.imagem,
                    categoriaId = produto.categoriaId
                )
            }
        }
    }

    fun onNomeChange(valor: String) {
        _uiState.update { it.copy(nome = valor) }
    }
    fun onVolumeChange(valor: String) {
        _uiState.update { it.copy(volume = valor) }
    }
    fun onEstabelecimentoChange(valor: String) {
        _uiState.update { it.copy(estabelecimento = valor) }
    }
    fun onPrecoChange(valor: String) {
        _uiState.update { it.copy(precoTexto = valor) }
    }
    fun onDescricaoChange(valor: String) {
        _uiState.update { it.copy(descricao = valor) }
    }
    fun onImagemChange(valor: Int) {
        _uiState.update { it.copy(imagem = valor) }
    }
    fun onCategoriaChange(valor: Int) {
        _uiState.update { it.copy(categoriaId = valor) }
    }

    fun onDescontoChange(valor: String) {
        _uiState.update { it.copy(descontoTexto = valor.filter { c -> c.isDigit() }.take(2)) }
    }

    fun onSalvar(aoSalvar: () -> Unit) {
        val state = _uiState.value
        _uiState.update { it.copy(tentouSalvar = true) }

        if (state.erros.temErro) {
            return
        }
        val preco = state.precoValor ?: return

        val produto = Produto(
            id = state.produtoEmEdicao?.id ?: 0,
            nome = state.nome.trim(),
            volume = state.volume.trim(),
            estabelecimento = state.estabelecimento.trim(),
            preco = preco,
            imagem = state.imagem,
            desconto = state.descontoTexto.ifBlank { null },
            descricao = state.descricao.trim(),
            categoriaId = state.categoriaId
        )

        viewModelScope.launch {
            if (state.editando) {
                produtoRepository.atualizarProduto(produto)
            } else {
                produtoRepository.inserirProduto(produto)
            }
            aoSalvar()
        }
    }
}