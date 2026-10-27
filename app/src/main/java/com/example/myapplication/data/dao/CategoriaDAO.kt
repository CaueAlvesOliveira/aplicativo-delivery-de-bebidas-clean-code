package com.example.myapplication.data.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.model.Categoria

@Dao
interface CategoriaDAO {
    @Insert
    suspend fun inserir(categoria: Categoria)

    @Update
    suspend fun atualizar(categoria: Categoria)

    @Delete
    suspend fun deletar(categoria: Categoria)

    @Query("DELETE FROM categorias WHERE id = :id")
    suspend fun deletarPorId(id: Int)

    @Query("SELECT * FROM categorias")
    suspend fun buscarCategorias(): List<Categoria>

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun buscarCategoriaPorId(id: Int): Categoria?
}