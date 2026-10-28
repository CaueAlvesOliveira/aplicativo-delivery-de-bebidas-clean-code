package com.example.myapplication.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.SportsBar
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.LojaRepository
import com.example.myapplication.model.Categoria
import com.example.myapplication.model.DadosFormProduto
import com.example.myapplication.model.ErrosFormProduto
import com.example.myapplication.model.ItemCarrinho
import com.example.myapplication.model.Produto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class LojaViewModel(contexto: Context) : ViewModel() {

    private val repository = LojaRepository(AppDatabase.getDatabase(contexto))

    val produtos = mutableStateListOf<Produto>()

    val categorias = mutableStateListOf<Categoria>()
    val carrinho = mutableStateListOf<ItemCarrinho>()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                popularBancoSeVazio()
                carregarDoBanco()
            }
        }
    }

    private suspend fun popularBancoSeVazio() {
        try {
            if (repository.buscarCategorias().isNotEmpty()) return

            repository.inserirCategoria(Categoria(nome = "Cerveja", icone = Icons.Outlined.SportsBar, cor = Color(0xFF29B6F6)))
            repository.inserirCategoria(Categoria(nome = "Vinho", icone = Icons.Outlined.WineBar, cor = Color(0xFF7E57C2)))
            repository.inserirCategoria(Categoria(nome = "Destilada", icone = Icons.Outlined.Liquor, cor = Color(0xFFFF7043)))
            repository.inserirCategoria(Categoria(nome = "Gelo", icone = Icons.Outlined.AcUnit, cor = Color(0xFF66BB6A)))
            repository.inserirCategoria(Categoria(nome = "Refrigerante", icone = Icons.Outlined.LocalDrink, cor = Color(0xFF8D6E63)))

            val categoriasSalvas = repository.buscarCategorias()
            fun idDaCategoria(nome: String): Int = categoriasSalvas.first { it.nome == nome }.id

            val cerveja = idDaCategoria("Cerveja")
            val vinho = idDaCategoria("Vinho")
            val destilada = idDaCategoria("Destilada")
            val refrigerante = idDaCategoria("Refrigerante")

            repository.inserirProduto(Produto(0, "Puro Malte", "350ml", "Cervejaria Noturna", 4.90, R.drawable.puro_malte, null, "Lager leve e refrescante, com final seco e pouco amargor. Ideal pra abrir a noite.", cerveja))
            repository.inserirProduto(Produto(0, "Vinho Tinto", "750ml", "Adega Vale Rubi", 32.90, R.drawable.vinho, null, "Tinto seco de corpo médio, com aroma de frutas vermelhas e taninos macios. Combina com massas e carnes.", vinho))
            repository.inserirProduto(Produto(0, "Energético", "2L", "Distribuidora Vo", 22.90, R.drawable.energetico, null, "Energético gelado de sabor intenso e cítrico, em garrafa de 2L. Rende para a galera toda.", refrigerante))
            repository.inserirProduto(Produto(0, "Cerveja Long Neck", "355ml", "Boteco Gelada", 7.90, R.drawable.cerveja, null, "Pilsen leve em long neck, sempre gelada. Boa pedida pro churrasco e pro fim de tarde.", cerveja))
            repository.inserirProduto(Produto(0, "Whisky", "1L", "Casa Highland", 89.90, R.drawable.whisky, null, "Blend suave com notas de baunilha e madeira. Para tomar puro, com gelo ou em drinks.", destilada))
            repository.inserirProduto(Produto(0, "Vodka", "1L", "Distribuidora Polar", 45.90, R.drawable.vodka, "20", "Destilado cristalino de sabor limpo. Base perfeita para drinks e caipiroskas.", destilada))
            repository.inserirProduto(Produto(0, "Gin", "750ml", "Botânico Gin Club", 79.90, R.drawable.gin, "10", "Notas de zimbro com toques cítricos e botânicos. Combina com tônica gelada e uma rodela de limão.", destilada))
            repository.inserirProduto(Produto(0, "Cachaça", "700ml", "Alambique Serra Dourada", 24.90, R.drawable.cachaca, "30", "Cachaça de sabor suave e toque adocicado de madeira. Ótima pura ou na caipirinha.", destilada))
            repository.inserirProduto(Produto(0, "Coca Cola", "2L", "Mercearia Dois Irmãos", 9.90, R.drawable.refrigerante, "35", "Refrigerante sabor cola, bem gelado e com gás. Perfeito para acompanhar pizza ou lanche.", refrigerante))
        } catch (e: Exception) {
            Log.e("Erro ao popular", "Msg: ${e.message}")
        }
    }

    private suspend fun carregarDoBanco() {
        try {
            categorias.clear()
            categorias.addAll(repository.buscarCategorias())
            produtos.clear()
            produtos.addAll(repository.buscarProdutos())
        } catch (e: Exception) {
            Log.e("LojaViewModel", "Erro ao carregar: ${e.message}")
        }
    }

    private fun executarEAtualizar(acao:suspend() -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    acao()
                } catch (e: Exception) {
                    Log.e("LojaViewModel", "Erro ao salvar: ${e.message}")
                }
                carregarDoBanco()
            }
        }
    }

    private fun converterPreco(texto: String): Double? {
        return texto.replace(',', '.').toDoubleOrNull()
    }

    fun textoDoPreco(preco: Double): String {
        return String.format(Locale.forLanguageTag("pt-BR"), "%.2f", preco)
    }

    fun filtrarDesconto(texto: String): String {
        return texto.filter { it.isDigit() }.take(2)
    }

    fun validarProduto(dados: DadosFormProduto): ErrosFormProduto {
        val preco = converterPreco(dados.precoTexto)
        return ErrosFormProduto(
            nome = if (dados.nome.isBlank()) "Informe o nome" else null,
            estabelecimento = if (dados.estabelecimento.isBlank()) "Informe o estabelecimento" else null,
            preco = if (preco == null || preco <= 0.0) "Informe um preço maior que zero" else null,
            categoria = if (buscarCategoria(dados.categoriaId) == null) "Selecione uma categoria" else null
        )
    }

    fun salvarProduto(produtoId: Int?, dados: DadosFormProduto): Boolean {
        if (validarProduto(dados).temErro) return false
        val preco = converterPreco(dados.precoTexto) ?: return false

        val nome = dados.nome.trim()
        val volume = dados.volume.trim()
        val estabelecimento = dados.estabelecimento.trim()
        val descricao = dados.descricao.trim()
        val desconto = dados.descontoTexto.ifBlank { null }

        if (produtoId != null) {
            editarProduto(
                produtoId, nome, volume, estabelecimento,
                preco, dados.imagem, desconto, descricao, dados.categoriaId
            )
        } else if (desconto != null) {
            adicionarProduto(
                nome, volume, estabelecimento,
                preco, dados.imagem, desconto, descricao, dados.categoriaId
            )
        } else {
            adicionarProduto(
                nome, volume, estabelecimento,
                preco, dados.imagem, descricao, dados.categoriaId
            )
        }
        return true
    }

    fun validarNomeCategoria(nome: String): String? {
        return if (nome.isBlank()) "Informe o nome" else null
    }

    fun salvarCategoria(categoriaId: Int?, nome: String, icone: ImageVector, cor: Color): Boolean {
        if (validarNomeCategoria(nome) != null) return false

        if (categoriaId != null) {
            editarCategoria(categoriaId, nome.trim(), icone, cor)
        } else {
            adicionarCategoria(nome.trim(), icone, cor)
        }
        return true
    }

    fun produtosOferta(): List<Produto> {
        return produtos.filter { !it.desconto.isNullOrBlank() }
    }

    fun produtosNormais(): List<Produto> {
        return produtos.filter { it.desconto.isNullOrBlank() }
    }

    fun adicionarProduto(
        nome: String,
        volume: String,
        estabelecimento: String,
        preco: Double,
        imagem: Int,
        desconto: String,
        descricao: String,
        categoriaId: Int) {
        executarEAtualizar {
            repository.inserirProduto(
                Produto(0, nome, volume, estabelecimento, preco, imagem, desconto, descricao, categoriaId)
            )
        }
    }

    fun adicionarProduto(
        nome: String,
        volume: String,
        estabelecimento: String,
        preco: Double,
        imagem: Int,
        descricao: String,
        categoriaId: Int) {
        executarEAtualizar {
            repository.inserirProduto(
                Produto(0, nome, volume, estabelecimento, preco, imagem, null, descricao, categoriaId)
            )
        }
    }

    fun removerProduto(produto: Produto) {
        carrinho.removeAll { it.produtoId == produto.id }
        executarEAtualizar { repository.deletarProduto(produto) }
    }

    fun editarProduto(
        id: Int,
        nome: String,
        volume: String,
        estabelecimento: String,
        preco: Double,
        imagem: Int,
        desconto: String?,
        descricao: String,
        categoriaId: Int) {
        executarEAtualizar {
            repository.atualizarProduto(
                Produto(id, nome, volume, estabelecimento, preco, imagem, desconto, descricao, categoriaId)
            )
        }
    }

    fun buscarProduto(id: Int): Produto? {
        return produtos.find { it.id == id }
    }

    suspend fun buscarProdutoPorIdNoBanco(id: Int): Produto? {
        return withContext(Dispatchers.IO) {
            try {
                repository.buscarProdutoPorId(id)
            } catch (e: Exception) {
                Log.e("LojaViewModel", "Erro ao buscar produto: ${e.message}")
                null
            }
        }
    }

    fun adicionarCategoria(nome: String, icone: ImageVector, cor: Color) {
        executarEAtualizar {
            repository.inserirCategoria(Categoria(0, nome, icone, cor))
        }
    }

    fun removerCategoria(categoria: Categoria) {
        val idsRemovidos = produtos.filter { it.categoriaId == categoria.id }.map { it.id }.toSet()
        carrinho.removeAll { it.produtoId in idsRemovidos }
        executarEAtualizar { repository.deletarCategoria(categoria) }
    }

    fun editarCategoria(id: Int, nome: String, icone: ImageVector, cor: Color) {
        executarEAtualizar {
            repository.atualizarCategoria(Categoria(id, nome, icone, cor))
        }
    }

    fun buscarCategoria(id: Int): Categoria? {
        return categorias.find { it.id == id }
    }

    suspend fun buscarCategoriaPorIdNoBanco(id: Int): Categoria? {
        return withContext(Dispatchers.IO) {
            try {
                repository.buscarCategoriaPorId(id)
            } catch (e: Exception) {
                Log.e("LojaViewModel", "Erro ao buscar categoria: ${e.message}")
                null
            }
        }
    }

    fun adicionarAoCarrinho(produtoId: Int, quantidade: Int = 1) {
        val i = carrinho.indexOfFirst { it.produtoId == produtoId }
        if (i == -1) {
            carrinho.add(ItemCarrinho(produtoId, quantidade))
        } else {
            carrinho[i] = carrinho[i].copy(quantidade = carrinho[i].quantidade + quantidade)
        }
    }

    fun alterarQuantidadeNoCarrinho(produtoId: Int, quantidade: Int) {
        if (quantidade <= 0) { removerDoCarrinho(produtoId); return }
        val i = carrinho.indexOfFirst { it.produtoId == produtoId }
        if (i != -1) carrinho[i] = carrinho[i].copy(quantidade = quantidade)
    }

    fun removerDoCarrinho(produtoId: Int) {
        carrinho.removeAll { it.produtoId == produtoId }
    }

    fun subtotalCarrinho(): Double {
        var subTotal = 0.0
        carrinho.forEach {
            val preco = buscarProduto(it.produtoId)?.preco ?: 0.0
            subTotal += it.quantidade * preco
        }
        return subTotal
    }

    fun calculaQuantidadeItensComprados(): Int {
        var quantidade = 0
        carrinho.forEach {
            quantidade += it.quantidade
        }
        return quantidade
    }

    fun calculaValorDaEntrega(): Double {
        val valorEntrega = 10.99
        return valorEntrega
    }

    fun calculaValorTotal(): Double {
        val valorTotal = subtotalCarrinho() + calculaValorDaEntrega()
        return valorTotal
    }
}