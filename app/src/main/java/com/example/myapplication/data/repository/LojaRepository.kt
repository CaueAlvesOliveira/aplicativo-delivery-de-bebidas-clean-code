package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.CarrinhoDAO
import com.example.myapplication.data.local.dao.CategoriaDAO
import com.example.myapplication.data.local.dao.ProdutoDAO
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.local.entity.ItemCarrinho
import com.example.myapplication.data.local.entity.Produto

class LojaRepository(db: AppDatabase) {

    private val produtoDAO: ProdutoDAO = db.produtoDAO()
    private val categoriaDAO: CategoriaDAO = db.categoriaDAO()
    private val carrinhoDAO: CarrinhoDAO = db.carrinhoDAO()

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

    suspend fun buscarCarrinho(): List<ItemCarrinho> = carrinhoDAO.buscarItens()

    suspend fun salvarItemCarrinho(item: ItemCarrinho) {
        carrinhoDAO.salvar(item)
    }

    suspend fun removerItemCarrinho(produtoId: Int) {
        carrinhoDAO.deletarPorProduto(produtoId)
    }

    suspend fun limparCarrinho() {
        carrinhoDAO.limpar()
    }
}