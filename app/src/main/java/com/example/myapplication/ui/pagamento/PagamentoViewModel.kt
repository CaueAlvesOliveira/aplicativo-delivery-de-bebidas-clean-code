package com.example.myapplication.ui.pagamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CarrinhoRepository
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PagamentoViewModel(
    private val carrinhoRepository: CarrinhoRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PagamentoUiState())

    val uiState: StateFlow<PagamentoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            carrinhoRepository.getAllCarrinho().collect { itens ->
                _uiState.update { currentState ->
                    currentState.copy(carrinho = itens)
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

    fun onConfirmarPagamento(aoConfirmar: () -> Unit) {
        viewModelScope.launch {
            carrinhoRepository.limparCarrinho()
            aoConfirmar()
        }
    }
}