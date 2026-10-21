package com.example.myapplication.data.dao

import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.model.Produto

interface ProdutoDAO {
    @Insert
    fun inserir(produto: Produto)

    @Update
    fun atualizar(produto: Produto)

    @Delete
    fun deletar(produto: Produto)

    @Query("DELETE FROM produtos WHERE id = :id")
    fun deletarPorId(id: Int)

    @Query("DELETE FROM produtos WHERE categoriaId = :categoriaId")
    fun deletarPorCategoria(categoriaId: Int)

    @Query("SELECT * FROM produtos")
    fun buscarProdutos(): List<Produto>

    @Query("SELECT * FROM produtos WHERE id = :id")
    fun buscarProdutoPorId(id: Int): Produto?

    @Query("SELECT * FROM produtos WHERE categoriaId = :categoriaId")
    fun buscarProdutosPorCategoria(categoriaId: Int): List<Produto>
}