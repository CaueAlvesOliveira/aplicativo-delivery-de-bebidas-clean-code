package com.example.myapplication.ui.carrinho

import com.example.myapplication.data.local.entity.ItemCarrinho
import com.example.myapplication.data.local.entity.Produto

data class CarrinhoUiState(
    val carrinho: List<ItemCarrinho> = emptyList(),
    val produtos: List<Produto> = emptyList()
) {
    fun produtoDoItem(item: ItemCarrinho): Produto? {
        return produtos.find { it.id == item.produtoId }
    }

    val subtotal: Double
        get() = carrinho.sumOf { item ->
            item.quantidade * (produtoDoItem(item)?.preco ?: 0.0)
        }

    val taxaEntrega: Double
        get() = 10.99
}