package com.example.myapplication.data.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.myapplication.model.ItemCarrinho


@Dao
interface CarrinhoDAO {
    @Upsert
    suspend fun salvar(item: ItemCarrinho)

    @Query("DELETE FROM carrinho WHERE produtoId = :produtoId")
    suspend fun deletarPorProduto(produtoId: Int)

    @Query("DELETE FROM carrinho")
    suspend fun limpar()

    @Query("SELECT * FROM carrinho")
    suspend fun buscarItens(): List<ItemCarrinho>
}