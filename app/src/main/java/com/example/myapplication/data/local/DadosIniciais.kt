package com.example.myapplication.data.local

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.SportsBar
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.ui.graphics.Color
import com.example.myapplication.R
import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.local.entity.Produto
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.first

suspend fun popularBancoSeVazio(
    categoriaRepository: CategoriaRepository,
    produtoRepository: ProdutoRepository
) {
    try {
        if (categoriaRepository.getAllCategorias().first().isNotEmpty()) return

        categoriaRepository.inserirCategoria(Categoria(nome = "Cerveja", icone = Icons.Outlined.SportsBar, cor = Color(0xFF29B6F6)))
        categoriaRepository.inserirCategoria(Categoria(nome = "Vinho", icone = Icons.Outlined.WineBar, cor = Color(0xFF7E57C2)))
        categoriaRepository.inserirCategoria(Categoria(nome = "Destilada", icone = Icons.Outlined.Liquor, cor = Color(0xFFFF7043)))
        categoriaRepository.inserirCategoria(Categoria(nome = "Gelo", icone = Icons.Outlined.AcUnit, cor = Color(0xFF66BB6A)))
        categoriaRepository.inserirCategoria(Categoria(nome = "Refrigerante", icone = Icons.Outlined.LocalDrink, cor = Color(0xFF8D6E63)))

        val categoriasSalvas = categoriaRepository.getAllCategorias().first()
        fun idDaCategoria(nome: String): Int = categoriasSalvas.first { it.nome == nome }.id

        val cerveja = idDaCategoria("Cerveja")
        val vinho = idDaCategoria("Vinho")
        val destilada = idDaCategoria("Destilada")
        val refrigerante = idDaCategoria("Refrigerante")

        produtoRepository.inserirProduto(Produto(0, "Puro Malte", "350ml", "Cervejaria Noturna", 4.90, R.drawable.puro_malte, null, "Lager leve e refrescante, com final seco e pouco amargor. Ideal pra abrir a noite.", cerveja))
        produtoRepository.inserirProduto(Produto(0, "Vinho Tinto", "750ml", "Adega Vale Rubi", 32.90, R.drawable.vinho, null, "Tinto seco de corpo médio, com aroma de frutas vermelhas e taninos macios. Combina com massas e carnes.", vinho))
        produtoRepository.inserirProduto(Produto(0, "Energético", "2L", "Distribuidora Vo", 22.90, R.drawable.energetico, null, "Energético gelado de sabor intenso e cítrico, em garrafa de 2L. Rende para a galera toda.", refrigerante))
        produtoRepository.inserirProduto(Produto(0, "Cerveja Long Neck", "355ml", "Boteco Gelada", 7.90, R.drawable.cerveja, null, "Pilsen leve em long neck, sempre gelada. Boa pedida pro churrasco e pro fim de tarde.", cerveja))
        produtoRepository.inserirProduto(Produto(0, "Whisky", "1L", "Casa Highland", 89.90, R.drawable.whisky, null, "Blend suave com notas de baunilha e madeira. Para tomar puro, com gelo ou em drinks.", destilada))
        produtoRepository.inserirProduto(Produto(0, "Vodka", "1L", "Distribuidora Polar", 45.90, R.drawable.vodka, "20", "Destilado cristalino de sabor limpo. Base perfeita para drinks e caipiroskas.", destilada))
        produtoRepository.inserirProduto(Produto(0, "Gin", "750ml", "Botânico Gin Club", 79.90, R.drawable.gin, "10", "Notas de zimbro com toques cítricos e botânicos. Combina com tônica gelada e uma rodela de limão.", destilada))
        produtoRepository.inserirProduto(Produto(0, "Cachaça", "700ml", "Alambique Serra Dourada", 24.90, R.drawable.cachaca, "30", "Cachaça de sabor suave e toque adocicado de madeira. Ótima pura ou na caipirinha.", destilada))
        produtoRepository.inserirProduto(Produto(0, "Coca Cola", "2L", "Mercearia Dois Irmãos", 9.90, R.drawable.refrigerante, "35", "Refrigerante sabor cola, bem gelado e com gás. Perfeito para acompanhar pizza ou lanche.", refrigerante))
    } catch (e: Exception) {
        Log.e("Erro ao popular", "Msg: ${e.message}")
    }
}