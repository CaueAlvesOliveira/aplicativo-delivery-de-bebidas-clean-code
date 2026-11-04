package com.example.myapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.myapplication.data.local.entity.ItemCarrinho
import kotlinx.coroutines.flow.Flow

@Dao
interface CarrinhoDAO {
    @Upsert
    suspend fun salvar(item: ItemCarrinho)
    @Query("DELETE FROM carrinho WHERE produtoId = :produtoId")
    suspend fun deletarPorProduto(produtoId: Int)

    @Query("DELETE FROM carrinho")
    suspend fun limpar()

    @Query("SELECT * FROM carrinho")
    fun buscarTodos(): Flow<List<ItemCarrinho>>

    @Query("SELECT * FROM carrinho WHERE produtoId = :produtoId")
    suspend fun buscarPorProduto(produtoId: Int): ItemCarrinho?
}