package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.CategoriaDAO
import com.example.myapplication.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

class CategoriaRepository (private val categoriaDAO: CategoriaDAO) {
    fun getAllCategorias(): Flow<List<Categoria>> {
        return categoriaDAO.buscarTodos()
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
        categoriaDAO.deletar(categoria)
    }
}