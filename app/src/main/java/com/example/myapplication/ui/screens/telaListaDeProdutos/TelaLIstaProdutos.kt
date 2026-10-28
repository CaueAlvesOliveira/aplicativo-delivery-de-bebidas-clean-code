package com.example.myapplication.ui.screens.telaListaDeProdutos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.local.entity.Produto
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela
import com.example.myapplication.viewmodel.LojaViewModel
import java.util.Locale

@Composable
fun TelaListaProdutos(
    viewModel: LojaViewModel,
    onVoltar: () -> Unit,
    onNovo: () -> Unit,
    onEditar: (Int) -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {},
    onAbrir: (Int) -> Unit,
) {

    var produtoParaExcluir by remember { mutableStateOf<Produto?>(null) }

    Scaffold(
        topBar = { TopBarTela("Produtos", onVoltar) },
        bottomBar = {
            BarraDeNavegacaoInferior(
                selectedIndex = 3,
                onInicio = onInicio,
                onCarrinho = onCarrinho,
                onPedidos = onPedidos,
                onProdutos = onProdutos
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNovo,
                containerColor = Color(0xFFFF7043),
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "Novo produto")
            }
        },
        containerColor = Color(0xFFECECEC),
        contentColor = Color.Black
    ) { innerPadding ->

        if (viewModel.produtos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum produto cadastrado.\nToque no + para adicionar.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.produtos, key = { it.id }) { produto ->
                    CardProdutoLista(
                        produto = produto,
                        nomeCategoria = viewModel.buscarCategoria(produto.categoriaId)?.nome,
                        onEditar = { onEditar(produto.id) },
                        onExcluir = { produtoParaExcluir = produto},
                        onAbrir = {onAbrir(produto.id)},
                    )
                }
            }
        }
    }

    produtoParaExcluir?.let { produto ->
        DialogExcluirProduto(
            nomeProduto = produto.nome,
            onConfirmar = {
                viewModel.removerProduto(produto)
                produtoParaExcluir = null
            },
            onCancelar = { produtoParaExcluir = null }
        )
    }
}

@Composable
fun CardProdutoLista(
    produto: Produto,
    nomeCategoria: String?,
    onEditar: () -> Unit,
    onExcluir: () -> Unit,
    onAbrir: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onAbrir()},
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = produto.imagem),
                contentDescription = produto.nome,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
            ) {
                Text(
                    text = produto.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = listOfNotNull(produto.volume, nomeCategoria).joinToString(" • "),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", produto.preco),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            IconButton(onClick = onEditar) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar ${produto.nome}",
                    tint = Color.Black
                )
            }

            IconButton(onClick = onExcluir) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir ${produto.nome}",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}

@Composable
fun DialogExcluirProduto(
    nomeProduto: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Excluir produto?") },
        text = { Text("\"$nomeProduto\" será removido da lista.") },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Excluir", color = Color(0xFFE53935))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}