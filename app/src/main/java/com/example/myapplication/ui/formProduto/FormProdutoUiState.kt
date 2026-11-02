package com.example.myapplication.ui.formProduto

import com.example.myapplication.R
import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.local.entity.Produto
import com.example.myapplication.model.ErrosFormProduto

data class FormProdutoUiState(
    val nome: String = "",
    val volume: String = "",
    val estabelecimento: String = "",
    val precoTexto: String = "",
    val descontoTexto: String = "",
    val descricao: String = "",
    val imagem: Int = R.drawable.puro_malte,
    val categoriaId: Int = -1,
    val categorias: List<Categoria> = emptyList(),
    val produtoEmEdicao: Produto? = null,
    val tentouSalvar: Boolean = false
) {
    val editando: Boolean
        get() = produtoEmEdicao != null

    val precoValor: Double?
        get() = precoTexto.replace(',', '.').toDoubleOrNull()

    val erros: ErrosFormProduto
        get() = ErrosFormProduto(
            nome = if (nome.isBlank()) "Informe o nome" else null,
            estabelecimento = if (estabelecimento.isBlank()) "Informe o estabelecimento" else null,
            preco = precoValor.let { if (it == null || it <= 0.0) "Informe um preço maior que zero" else null },
            categoria = if (categorias.none { it.id == categoriaId }) "Selecione uma categoria" else null
        )
}