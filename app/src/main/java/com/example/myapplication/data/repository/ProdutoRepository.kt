package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ProdutoDAO
import com.example.myapplication.data.local.entity.Produto
import kotlinx.coroutines.flow.Flow

class ProdutoRepository (private val produtoDAO: ProdutoDAO) {
    fun getAllProdutos(): Flow<List<Produto>> {
        return produtoDAO.buscarTodos()
    }

    suspend fun inserirProduto(produto: Produto) {
        produtoDAO.inserir(produto)
    }

    suspend fun atualizarProduto(produto: Produto) {
        produtoDAO.atualizar(produto)
    }

    suspend fun deletarProduto(produto: Produto) {
        produtoDAO.deletar(produto)
    }

    suspend fun buscarProdutoPorId(id: Int): Produto? {
        return produtoDAO.buscarProdutoPorId(id)
    }
}
