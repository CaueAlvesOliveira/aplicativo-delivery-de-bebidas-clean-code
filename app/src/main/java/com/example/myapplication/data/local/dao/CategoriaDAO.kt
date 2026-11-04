package com.example.myapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

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
    fun buscarTodos(): Flow<List<Categoria>>

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun buscarCategoriaPorId(id: Int): Categoria?
}