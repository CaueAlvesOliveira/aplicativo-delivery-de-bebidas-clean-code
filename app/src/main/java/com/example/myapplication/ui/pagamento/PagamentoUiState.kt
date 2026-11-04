package com.example.myapplication.ui.pagamento

import com.example.myapplication.data.local.entity.ItemCarrinho
import com.example.myapplication.data.local.entity.Produto

data class PagamentoUiState(
    val carrinho: List<ItemCarrinho> = emptyList(),
    val produtos: List<Produto> = emptyList()
) {
    val quantidadeItens: Int
        get() = carrinho.sumOf { it.quantidade }

    val subtotal: Double
        get() = carrinho.sumOf { item ->
            item.quantidade * (produtos.find { it.id == item.produtoId }?.preco ?: 0.0)
        }

    val taxaEntrega: Double
        get() = 10.99

    val total: Double
        get() = subtotal + taxaEntrega
}