package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.CarrinhoDAO
import com.example.myapplication.data.local.entity.ItemCarrinho
import kotlinx.coroutines.flow.Flow

class CarrinhoRepository (private val carrinhoDAO: CarrinhoDAO) {
    fun getAllCarrinho(): Flow<List<ItemCarrinho>> {
        return carrinhoDAO.buscarTodos()
    }

    suspend fun salvarItemCarrinho(item: ItemCarrinho) {
        carrinhoDAO.salvar(item)
    }

    suspend fun removerItemCarrinho(produtoId: Int) {
        carrinhoDAO.deletarPorProduto(produtoId)
    }

    suspend fun limparCarrinho() {
        carrinhoDAO.limpar()
    }

    suspend fun adicionarAoCarrinho(produtoId: Int, quantidade: Int) {

        val itemEncontrado = carrinhoDAO.buscarPorProduto(produtoId)

        carrinhoDAO.salvar(
            ItemCarrinho(
                produtoId = produtoId,
                quantidade = (itemEncontrado?.quantidade ?: 0) + quantidade
            )
        )
    }
}