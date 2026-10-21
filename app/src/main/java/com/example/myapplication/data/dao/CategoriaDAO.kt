package com.example.myapplication.data.dao

import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.model.Categoria

interface CategoriaDAO {
    @Insert
    fun inserir(categoria: Categoria)

    @Update
    fun atualizar(categoria: Categoria)

    @Delete
    fun deletar(categoria: Categoria)

    @Query("DELETE FROM categorias WHERE id = :id")
    fun deletarPorId(id: Int)

    @Query("SELECT * FROM categorias")
    fun buscarCategorias(): List<Categoria>

    @Query("SELECT * FROM categorias WHERE id = :id")
    fun buscarCategoriaPorId(id: Int): Categoria?
}