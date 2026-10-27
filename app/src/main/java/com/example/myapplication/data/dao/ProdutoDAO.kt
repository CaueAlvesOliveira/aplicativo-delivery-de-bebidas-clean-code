package com.example.myapplication.data.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.model.Produto

@Dao
interface ProdutoDAO {
    @Insert
    suspend fun inserir(produto: Produto)

    @Update
    suspend fun atualizar(produto: Produto)

    @Delete
    suspend fun deletar(produto: Produto)

    @Query("DELETE FROM produtos WHERE id = :id")
    suspend fun deletarPorId(id: Int)

    @Query("DELETE FROM produtos WHERE categoriaId = :categoriaId")
    suspend fun deletarPorCategoria(categoriaId: Int)

    @Query("SELECT * FROM produtos")
    suspend fun buscarProdutos(): List<Produto>

    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarProdutoPorId(id: Int): Produto?

    @Query("SELECT * FROM produtos WHERE categoriaId = :categoriaId")
    suspend fun buscarProdutosPorCategoria(categoriaId: Int): List<Produto>
}