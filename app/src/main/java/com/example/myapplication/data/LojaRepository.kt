package com.example.myapplication.data

import com.example.myapplication.data.dao.CategoriaDAO
import com.example.myapplication.data.dao.ProdutoDAO
import com.example.myapplication.model.Categoria
import com.example.myapplication.model.Produto

class LojaRepository(db: AppDatabase) {

    private val produtoDAO: ProdutoDAO = db.produtoDAO()
    private val categoriaDAO: CategoriaDAO = db.categoriaDAO()

    suspend fun buscarProdutos(): List<Produto> {
        return produtoDAO.buscarProdutos()
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

    suspend fun buscarCategorias(): List<Categoria> {
        return categoriaDAO.buscarCategorias()
    }

    suspend fun buscarCategoriaPorId(id: Int): Categoria? {
        return categoriaDAO.buscarCategoriaPorId(id)
    }

    suspend fun inserirCategoria(categoria: Categoria) {
        categoriaDAO.inserir(categoria)
    }

    suspend fun atualizarCategoria(categoria: Categoria) {
        categoriaDAO.atualizar(categoria)
    }

    suspend fun deletarCategoria(categoria: Categoria) {
        produtoDAO.deletarPorCategoria(categoria.id)
        categoriaDAO.deletar(categoria)
    }
}