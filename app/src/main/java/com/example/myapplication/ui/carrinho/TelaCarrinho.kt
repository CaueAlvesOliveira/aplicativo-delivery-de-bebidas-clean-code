package com.example.myapplication.ui.carrinho

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Produto
import com.example.myapplication.data.repository.CarrinhoRepository
import com.example.myapplication.data.repository.ProdutoRepository
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela
import com.example.myapplication.ui.produto.BotaoQuantidade
import java.util.Locale

private val CorFundo = Color(0xFFECECEC)
private val CorBorda = Color(0xFFD7D7D7)
private val CorDivisor = Color(0xFFE1E1E1)
private val CorLaranja = Color(0xFFEB500E)
private val CorVerde = Color(0xFF60BF65)
private val CorAplicar = Color(0xFF5E87B9)
private val PaddingHorizontal = 24.dp

@Composable
fun TelaCarrinho(
    onVoltar: () -> Unit,
    onIrParaPagamento: () -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {},
    viewModel: CarrinhoViewModel = viewModel(
        factory = CarrinhoViewModelFactory(
            CarrinhoRepository(AppDatabase.getDatabase(LocalContext.current).carrinhoDAO()),
            ProdutoRepository(AppDatabase.getDatabase(LocalContext.current).produtoDAO())
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopBarTela("Seu carrinho", onVoltar) },
        bottomBar = {
            BarraDeNavegacaoInferior(
                selectedIndex = 1,
                onInicio = onInicio,
                onCarrinho = onCarrinho,
                onPedidos = onPedidos,
                onProdutos = onProdutos
            )
        },
        containerColor = CorFundo,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            if (uiState.carrinho.isEmpty()) {
                CarrinhoVazio(
                    onContinuarComprando = onVoltar,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = PaddingHorizontal, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.carrinho, key = { it.produtoId }) { item ->
                        uiState.produtoDoItem(item)?.let { produto ->
                            ItemDoCarrinho(
                                produto = produto,
                                quantidade = item.quantidade,
                                onMenos = {
                                    viewModel.onAlterarQuantidade(item.produtoId, item.quantidade - 1)
                                },
                                onMais = {
                                    viewModel.onAlterarQuantidade(item.produtoId, item.quantidade + 1)
                                }
                            )
                        }
                    }

                    item {
                        Spacer(Modifier.height(8.dp))
                    }
                }

                ResumoDoPedido(
                    subtotal = uiState.subtotal,
                    taxaEntrega = uiState.taxaEntrega,
                    onIrParaPagamento = onIrParaPagamento
                )
            }
        }
    }
}

@Composable
private fun ItemDoCarrinho(
    produto: Produto,
    quantidade: Int,
    onMenos: () -> Unit,
    onMais: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .background(Color.White, RoundedCornerShape(18.dp))
                .padding(6.dp)
        ) {
            Image(
                painter = painterResource(id = produto.imagem),
                contentDescription = produto.nome,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "${produto.nome} ${produto.volume}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = precoEmReais(produto.preco),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        StepperQuantidade(
            quantidade = quantidade,
            onMenos = onMenos,
            onMais = onMais
        )
    }
}

@Composable
private fun StepperQuantidade(
    quantidade: Int,
    onMenos: () -> Unit,
    onMais: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(50))
            .border(1.dp, CorBorda, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        BotaoQuantidadeCarrinho(
            icone = Icons.Default.Remove,
            descricao = "Diminuir quantidade",
            onClick = onMenos
        )

        Text(
            text = "$quantidade",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(32.dp)
        )

        BotaoQuantidadeCarrinho(
            icone = Icons.Default.Add,
            descricao = "Aumentar quantidade",
            onClick = onMais
        )
    }
}

@Composable
private fun ResumoDoPedido(
    subtotal: Double,
    taxaEntrega: Double,
    onIrParaPagamento: () -> Unit
) {
    val total = subtotal + taxaEntrega

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .height(1.dp)
                .background(CorDivisor)
        )

        Column(
            modifier = Modifier.padding(
                start = PaddingHorizontal,
                end = PaddingHorizontal,
                top = 16.dp,
                bottom = 16.dp
            )
        ) {
            LinhaResumo(rotulo = "Subtotal", valor = precoEmReais(subtotal))

            Spacer(Modifier.height(8.dp))

            LinhaResumo(
                rotulo = "Taxa de entrega",
                valor = if (taxaEntrega == 0.0) "Grátis" else precoEmReais(taxaEntrega),
                corValor = if (taxaEntrega == 0.0) CorVerde else Color.Black
            )

            Spacer(Modifier.height(8.dp))

            LinhaResumo(rotulo = "Total", valor = precoEmReais(total), destaque = true)

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onIrParaPagamento,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = CorLaranja),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Ir para pagamento",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LinhaResumo(
    rotulo: String,
    valor: String,
    corValor: Color = Color.Black,
    destaque: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rotulo,
            fontSize = 16.sp,
            color = if (destaque) Color.Black else Color.Gray,
            fontWeight = if (destaque) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = valor,
            fontSize = 16.sp,
            color = corValor
        )
    }
}

@Composable
private fun CarrinhoVazio(
    onContinuarComprando: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Seu carrinho está vazio",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Adicione produtos para fazer o seu pedido.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onContinuarComprando,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = CorLaranja)
        ) {
            Text(
                text = "Continuar comprando",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun precoEmReais(valor: Double): String {
    return String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", valor)
}

@Composable
fun BotaoQuantidadeCarrinho(icone: ImageVector, descricao: String, onClick: () -> Unit) {
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