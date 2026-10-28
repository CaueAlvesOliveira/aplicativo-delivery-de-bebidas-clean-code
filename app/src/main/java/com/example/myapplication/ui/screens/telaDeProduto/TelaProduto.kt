package com.example.myapplication.ui.screens.telaDeProduto

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.local.entity.Produto
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.BotaoVoltar
import com.example.myapplication.viewmodel.LojaViewModel
import java.util.Locale

@Composable
fun TelaProduto(
    produtoId: Int,
    viewModel: LojaViewModel,
    onVoltar: () -> Unit,
    irParaCarrinho: () -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {}
) {
    var produto by remember { mutableStateOf<Produto?>(null) }
    var carregou by remember { mutableStateOf(false) }
    var quantidade by rememberSaveable { mutableIntStateOf(1) }
    val contexto = LocalContext.current

    LaunchedEffect(produtoId) {
        produto = viewModel.buscarProdutoPorIdNoBanco(produtoId)
        carregou = true
    }

    if (!carregou) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val produtoAtual = produto
    if (produtoAtual == null) {
        LaunchedEffect(Unit) { onVoltar() }
        return
    }

    Scaffold(
        bottomBar = {
            BarraDeNavegacaoInferior(
                selectedIndex = 3,
                onInicio = onInicio,
                onCarrinho = onCarrinho,
                onPedidos = onPedidos,
                onProdutos = onProdutos
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            color = Color(0xFFECECEC),
            contentColor = Color.Black
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BotaoVoltar(onVoltar)
                    BotaoFavorito()
                }

                Spacer(Modifier.height(16.dp))
                ImagemProduto(produtoAtual)
                Spacer(Modifier.height(20.dp))
                InformacoesProduto(produtoAtual)
                Spacer(Modifier.height(20.dp))

                SeletorQuantidade(
                    quantidade = quantidade,
                    onQuantidadeChange = { quantidade = it }
                )

                Spacer(Modifier.weight(1f))

                RodapeCompra(
                    produto = produtoAtual,
                    quantidade = quantidade,
                    onAdicionar = {
                        viewModel.adicionarAoCarrinho(produtoAtual.id, quantidade)
                        Toast.makeText(contexto, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show()
                        irParaCarrinho()
                    }
                )
            }
        }
    }
}

@Composable
fun BotaoFavorito() {

    var favoritado by remember { mutableStateOf(false) }

    IconButton(
        onClick = {favoritado = !favoritado},
        modifier = Modifier
            .size(48.dp)
            .background(Color.White, shape = CircleShape)
            .border(1.dp, Color.LightGray, shape = CircleShape),
    ) {
        Icon(
            imageVector = if (favoritado) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favoritar produto",
            tint = Color(0xFFE53935),
        )
    }
}

@Composable
fun ImagemProduto(produto: Produto) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = produto.imagem),
            contentDescription = produto.nome,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun InformacoesProduto(produto: Produto) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = produto.estabelecimento,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5B8DEF)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = produto.nome,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TagProduto(texto = produto.volume,  color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = produto.descricao,
            fontSize = 14.sp,
            color = Color(0xFF3D3D3D)
        )
    }
}

@Composable
fun TagProduto(texto: String, color: Color) {
    Box(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(50))
            .border(1.dp, color, shape = RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = texto,
            fontSize = 12.sp,
            color = color
        )
    }
}

@Composable
fun SeletorQuantidade(
    quantidade: Int,
    onQuantidadeChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Quantidade",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(50))
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            BotaoQuantidade(
                Icons.Default.Remove,
                descricao = "Diminuir quantidade",
                onClick = { if (quantidade > 1) onQuantidadeChange(quantidade - 1) }
            )

            Text(
                text = "$quantidade",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )

            BotaoQuantidade(
                Icons.Default.Add,
                descricao = "Aumentar quantidade",
                onClick = { onQuantidadeChange(quantidade + 1) }
            )
        }
    }
}

@Composable
fun BotaoQuantidade(icone: ImageVector, descricao: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color(0xFFD6EFFB))
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icone,
            contentDescription = descricao,
            tint = Color.Black,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun RodapeCompra(produto: Produto, quantidade: Int, onAdicionar: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            if (!produto.desconto.isNullOrBlank()) {
                Text(
                    text = String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", produto.preco),
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )
            }
            Text(
                text = String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", produto.preco * quantidade),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onAdicionar,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043)),
            modifier = Modifier.width(160.dp)
        ) {
            Text(
                text = "Adicionar ao carrinho",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}