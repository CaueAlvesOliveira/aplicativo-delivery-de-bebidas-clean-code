package com.example.myapplication.ui.screens.telaDeRastreio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.model.EstadoEtapa
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela

@Composable
fun TelaRastreio(
    onVoltar: () -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {}
) {
    Scaffold (
        topBar = { TopBarTela("Pedido", onVoltar) },
        bottomBar = {
            BarraDeNavegacaoInferior(
                selectedIndex = 2,
                onInicio = onInicio,
                onCarrinho = onCarrinho,
                onPedidos = onPedidos,
                onProdutos = onProdutos
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = Color(0xFFECECEC)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                MapaEntrega()

                Spacer(modifier = Modifier.height(16.dp))

                TempoEstimado()

                Spacer(modifier = Modifier.height(24.dp))

                EtapasPedido()

                Spacer(modifier = Modifier.height(20.dp))

                CardEntregador()
            }
        }
    }
}

@Composable
fun MapaEntrega() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.mapa),
            contentDescription = "Mapa com a localização da entrega",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color(0xFF0D2B52), shape = RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Pedido #4521",
                color = Color(0xFF4FC3F7),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Localização da entrega",
            tint = Color(0xFF1976D2),
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(44.dp)
                .background(Color(0xFFFF7043), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.PedalBike,
                contentDescription = "Entregador a caminho",
                tint = Color.Black,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun TempoEstimado() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CHEGA EM",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF757575)
        )
        Text(
            text = "12 min",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun EtapasPedido() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EtapaPedido(Icons.Default.Check, "Confirmado", EstadoEtapa.CONCLUIDA, Modifier.weight(1f))
        LinhaConectora(concluida = true)
        EtapaPedido(Icons.Default.Check, "Preparando", EstadoEtapa.CONCLUIDA, Modifier.weight(1f))
        LinhaConectora(concluida = true)
        EtapaPedido(Icons.Default.PedalBike, "A caminho", EstadoEtapa.ATUAL, Modifier.weight(1f))
        LinhaConectora(concluida = false)
        EtapaPedido(Icons.Default.Home, "Entregue", EstadoEtapa.PENDENTE, Modifier.weight(1f))
    }
}

@Composable
fun LinhaConectora(concluida: Boolean) {
    Box(
        modifier = Modifier
            .width(20.dp)
            .height(1.dp)
            .background(if (concluida) Color.Black else Color(0xFFD9D9D9))
    )
}

@Composable
fun EtapaPedido(icone: ImageVector, texto: String, estado: EstadoEtapa, modifier: Modifier = Modifier) {
    val cor = when (estado) {
        EstadoEtapa.CONCLUIDA -> Color(0xFF4CAF50)
        EstadoEtapa.ATUAL -> Color(0xFFE53935)
        EstadoEtapa.PENDENTE -> Color(0xFFBDBDBD)
    }
    val corIcone = when (estado) {
        EstadoEtapa.CONCLUIDA -> Color(0xFF4CAF50)
        EstadoEtapa.ATUAL -> Color.Black
        EstadoEtapa.PENDENTE -> Color(0xFF9E9E9E)
    }
    val corTexto = when (estado) {
        EstadoEtapa.ATUAL -> Color(0xFFE53935)
        EstadoEtapa.PENDENTE -> Color(0xFF9E9E9E)
        else -> Color(0xFF1A1A1A)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(Color.White, shape = CircleShape)
                .then(
                    if (estado == EstadoEtapa.CONCLUIDA) {
                        Modifier.background(Color.White, CircleShape)
                    } else Modifier
                )
                .drawBehind {
                    if (estado == EstadoEtapa.CONCLUIDA) {
                        drawCircle(
                            color = cor,
                            radius = size.minDimension / 2 - 2.dp.toPx(),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    } else {
                        drawCircle(
                            color = cor,
                            radius = size.minDimension / 2 - 2.dp.toPx(),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f))
                            )
                        )
                    }
                }
        ) {
            Icon(
                imageVector = icone,
                contentDescription = texto,
                tint = corIcone,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = texto,
            fontSize = 12.sp,
            fontWeight = if (estado == EstadoEtapa.ATUAL) FontWeight.Bold else FontWeight.Normal,
            color = corTexto
        )
    }
}

@Composable
fun CardEntregador() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFFD6EFFB), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Foto do entregador",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Carlos - Moto ABC-1234",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Seu entregador",
                fontSize = 13.sp,
                color = Color(0xFF9E9E9E)
            )
        }

        BotaoIconeCircular(
            Icons.Default.Call,
            "Ligar para o entregador",
        )

        Spacer(modifier = Modifier.width(8.dp))

        BotaoIconeCircular(
            Icons.AutoMirrored.Filled.Chat,
            "Enviar mensagem para o entregador",
        )
    }
}

@Composable
fun BotaoIconeCircular(icone: ImageVector, descricao: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFF0F0F0))
    ) {
        Icon(
            imageVector = icone,
            contentDescription = descricao,
            tint = Color.Black,
            modifier = Modifier.size(18.dp)
        )
    }
}