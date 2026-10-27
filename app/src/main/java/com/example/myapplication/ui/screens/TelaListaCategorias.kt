package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.model.Categoria
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela
import com.example.myapplication.viewmodel.LojaViewModel

@Composable
fun TelaListaCategorias(
    viewModel: LojaViewModel,
    onVoltar: () -> Unit,
    onNovo: () -> Unit,
    onEditar: (Int) -> Unit,
    onAbrir: (Int) -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {}
) {

    var categoriaParaExcluir by remember { mutableStateOf<Categoria?>(null) }

    Scaffold(
        topBar = { TopBarTela("Categorias", onVoltar) },
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
                Icon(Icons.Default.Add, contentDescription = "Nova categoria")
            }
        },
        containerColor = Color(0xFFECECEC),
        contentColor = Color.Black
    ) { innerPadding ->

        if (viewModel.categorias.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma categoria cadastrada.\nToque no + para adicionar.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.categorias, key = { it.id }) { categoria ->
                    CardCategoriaLista(
                        categoria = categoria,
                        quantidadeProdutos = viewModel.produtos.count { it.categoriaId == categoria.id },
                        onAbrir = { onAbrir(categoria.id) },
                        onEditar = { onEditar(categoria.id) },
                        onExcluir = { categoriaParaExcluir = categoria }
                    )
                }
            }
        }
    }

    categoriaParaExcluir?.let { categoria ->
        DialogExcluirCategoria(
            nomeCategoria = categoria.nome,
            quantidadeProdutos = viewModel.produtos.count { it.categoriaId == categoria.id },
            onConfirmar = {
                viewModel.removerCategoria(categoria)
                categoriaParaExcluir = null
            },
            onCancelar = { categoriaParaExcluir = null }
        )
    }
}

@Composable
private fun CardCategoriaLista(
    categoria: Categoria,
    quantidadeProdutos: Int,
    onAbrir: () -> Unit,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onAbrir() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .background(categoria.cor.copy(alpha = 0.15f), shape = CircleShape)
            ) {
                Icon(
                    imageVector = categoria.icone,
                    contentDescription = "Ícone da categoria ${categoria.nome}",
                    tint = categoria.cor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
            ) {
                Text(
                    text = categoria.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (quantidadeProdutos == 1) "1 produto" else "$quantidadeProdutos produtos",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onEditar) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar ${categoria.nome}",
                    tint = Color.Black
                )
            }

            IconButton(onClick = onExcluir) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir ${categoria.nome}",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}

@Composable
private fun DialogExcluirCategoria(
    nomeCategoria: String,
    quantidadeProdutos: Int,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Excluir categoria?") },
        text = {
            Text(
                if (quantidadeProdutos > 0)
                    "\"$nomeCategoria\" será removida junto com os $quantidadeProdutos produto(s) dessa categoria."
                else
                    "\"$nomeCategoria\" será removida da lista."
            )
        },
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