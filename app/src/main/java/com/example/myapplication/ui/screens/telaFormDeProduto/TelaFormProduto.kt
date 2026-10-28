package com.example.myapplication.ui.screens.telaFormDeProduto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.model.DadosFormProduto
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela
import com.example.myapplication.viewmodel.LojaViewModel

private val IMAGENS_DISPONIVEIS = listOf(
    R.drawable.puro_malte,
    R.drawable.cerveja,
    R.drawable.vinho,
    R.drawable.whisky,
    R.drawable.vodka,
    R.drawable.gin,
    R.drawable.cachaca,
    R.drawable.refrigerante,
    R.drawable.energetico
)

@Composable
fun TelaFormProduto(
    produtoId: Int,
    viewModel: LojaViewModel,
    onVoltar: () -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {}
) {
    val existente = viewModel.buscarProduto(produtoId)
    val editando = existente != null

    var nome by rememberSaveable { mutableStateOf(existente?.nome ?: "") }
    var volume by rememberSaveable { mutableStateOf(existente?.volume ?: "") }
    var estabelecimento by rememberSaveable { mutableStateOf(existente?.estabelecimento ?: "") }
    var precoTexto by rememberSaveable {
        mutableStateOf(existente?.let { viewModel.textoDoPreco(it.preco) } ?: "")
    }
    var descontoTexto by rememberSaveable { mutableStateOf(existente?.desconto ?: "") }
    var descricao by rememberSaveable { mutableStateOf(existente?.descricao ?: "") }
    var imagem by rememberSaveable { mutableIntStateOf(existente?.imagem ?: IMAGENS_DISPONIVEIS.first()) }
    var categoriaId by rememberSaveable {
        mutableIntStateOf(existente?.categoriaId ?: viewModel.categorias.firstOrNull()?.id ?: -1)
    }
    var tentouSalvar by rememberSaveable { mutableStateOf(false) }

    val dados = DadosFormProduto(
        nome = nome,
        volume = volume,
        estabelecimento = estabelecimento,
        precoTexto = precoTexto,
        descontoTexto = descontoTexto,
        descricao = descricao,
        imagem = imagem,
        categoriaId = categoriaId
    )
    val erros = viewModel.validarProduto(dados)

    fun salvar() {
        tentouSalvar = true
        if (viewModel.salvarProduto(existente?.id, dados)) {
            onVoltar()
        }
    }

    Scaffold(
        topBar = { TopBarTela(if (editando) "Editar produto" else "Novo produto", onVoltar) },
        bottomBar = {
            BarraDeNavegacaoInferior(
                selectedIndex = 3,
                onInicio = onInicio,
                onCarrinho = onCarrinho,
                onPedidos = onPedidos,
                onProdutos = onProdutos
            )
        },
        containerColor = Color(0xFFECECEC),
        contentColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoTexto(
                valor = nome,
                onChange = { nome = it },
                rotulo = "Nome",
                erro = if (tentouSalvar) erros.nome else null
            )

            CampoTexto(
                valor = volume,
                onChange = { volume = it },
                rotulo = "Volume (ex: 350ml)"
            )

            CampoTexto(
                valor = estabelecimento,
                onChange = { estabelecimento = it },
                rotulo = "Estabelecimento",
                erro = if (tentouSalvar) erros.estabelecimento else null
            )

            CampoTexto(
                valor = precoTexto,
                onChange = { precoTexto = it },
                rotulo = "Preço (R$)",
                teclado = KeyboardType.Decimal,
                erro = if (tentouSalvar) erros.preco else null
            )

            CampoTexto(
                valor = descontoTexto,
                onChange = { descontoTexto = viewModel.filtrarDesconto(it) },
                rotulo = "Desconto em % (opcional)",
                teclado = KeyboardType.Number
            )

            CampoTexto(
                valor = descricao,
                onChange = { descricao = it },
                rotulo = "Descrição",
                minLinhas = 3
            )

            Text("Categoria", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.categorias, key = { it.id }) { categoria ->
                    FilterChip(
                        selected = categoria.id == categoriaId,
                        onClick = { categoriaId = categoria.id },
                        label = { Text(categoria.nome) }
                    )
                }
            }
            if (tentouSalvar && erros.categoria != null) {
                Text(erros.categoria, color = Color(0xFFE53935), fontSize = 12.sp)
            }

            Text("Imagem", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(IMAGENS_DISPONIVEIS) { recurso ->
                    val selecionada = recurso == imagem
                    Image(
                        painter = painterResource(id = recurso),
                        contentDescription = "Opção de imagem",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(
                                width = if (selecionada) 3.dp else 1.dp,
                                color = if (selecionada) Color(0xFFFF7043) else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { imagem = recurso }
                            .padding(8.dp)
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = { salvar() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043))
            ) {
                Text(
                    text = if (editando) "Salvar alterações" else "Adicionar",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CampoTexto(
    valor: String,
    onChange: (String) -> Unit,
    rotulo: String,
    teclado: KeyboardType = KeyboardType.Text,
    minLinhas: Int = 1,
    erro: String? = null
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(rotulo) },
        isError = erro != null,
        supportingText = if (erro != null) {
            { Text(erro) }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        singleLine = minLinhas == 1,
        minLines = minLinhas,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}