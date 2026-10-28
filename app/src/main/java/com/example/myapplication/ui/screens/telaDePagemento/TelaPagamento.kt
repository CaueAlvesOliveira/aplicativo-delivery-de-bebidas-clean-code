package com.example.myapplication.ui.screens.telaDePagemento

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.navegacao.rotas.Rotas
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela
import com.example.myapplication.viewmodel.LojaViewModel

@Composable
fun TelaPagamento(
    onVoltar: () -> Unit,
    navController: NavController,
    viewModel: LojaViewModel,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {}
){

    Scaffold(
        topBar = {
            TopBarTela(
                titulo = "Pagamento",
                onVoltar = onVoltar,
                corFundo = MaterialTheme.colorScheme.background
            )
        },
        bottomBar = {
            BarraDeNavegacaoInferior(
                selectedIndex = 1,
                onInicio = onInicio,
                onCarrinho = onCarrinho,
                onPedidos = onPedidos,
                onProdutos = onProdutos
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardsModeloPagamento()
            CardResumo(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            BotaoConfirmarPagamento(
                onConfirmar = {
                    viewModel.limparCarrinho()
                    navController.navigate(Rotas.RASTREIO)
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TextoTitulo(){
    Text(
        "Pagamento",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(10.dp)
    )
}

@Composable
fun CardsModeloPagamento() {

    var itemSelecionado by remember { mutableStateOf(0) }

    val itens = listOf(
        Triple("Pix", Icons.Default.Add, 0),
        Triple("Cartão de Crédito", Icons.Default.Search, 1),
        Triple("Dinheiro na Entrega", Icons.Default.List, 2),
    )

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        itens.forEach { (nome, icone, index) ->

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(
                        width = if (itemSelecionado == index) 2.dp else 1.dp,
                        color = if (itemSelecionado == index) Color.Green else Color.Gray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { itemSelecionado = index },
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icone,
                        contentDescription = nome,
                        modifier = Modifier.size(24.dp),
                        tint = if (itemSelecionado == index) Color.Green else Color.Gray
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = nome,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun CardResumo(viewModel: LojaViewModel){

    var itemSelecionado by remember { mutableStateOf(0) }

    val quantidadeItens = viewModel.calculaQuantidadeItensComprados()
    val valorTotalProdutos = viewModel.subtotalCarrinho()
    val valorEntrega = viewModel.calculaValorDaEntrega()
    val valorTotal = viewModel.calculaValorTotal()

    Spacer(modifier = Modifier.width(25.dp))
    Column(
        modifier = Modifier.padding(17.dp)
    ) {
        Text(
            "Resumo",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .border(
                    color = Color.Gray,
                    shape = RoundedCornerShape(12.dp),
                    width = 1.dp
                ),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "$quantidadeItens itens")
                    Text(text = "R$ %.2f".format(valorTotalProdutos))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Entrega expressa")
                    Text(text = "R$ %.2f".format(valorEntrega))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "R$ %.2f".format(valorTotal),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BotaoConfirmarPagamento(onConfirmar: () -> Unit){

    Column(
        modifier = Modifier.padding(16.dp, 15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onConfirmar,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(255, 112, 67, 255)
            ),
            modifier = Modifier.size(300.dp, 50.dp)
        ) {
            Text(
                fontSize = 17.sp,
                text = "Confirmar Pedido"
            )
        }

        Row(
            modifier = Modifier.padding(start = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Lock, "cadeado", modifier = Modifier.size(15.dp))
            Text(
                fontSize = 15.sp,
                text = "Pagamento Seguro"
            )
        }
    }
}