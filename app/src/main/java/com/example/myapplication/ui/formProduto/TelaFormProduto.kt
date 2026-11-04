package com.example.myapplication.ui.formProduto

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.data.repository.ProdutoRepository
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela

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
    onVoltar: () -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {},
    viewModel: FormProdutoViewModel = viewModel(
        factory = FormProdutoViewModelFactory(
            produtoId,
            ProdutoRepository(AppDatabase.getDatabase(LocalContext.current).produtoDAO()),
            CategoriaRepository(AppDatabase.getDatabase(LocalContext.current).categoriaDAO())
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val erros = uiState.erros

    Scaffold(
        topBar = { TopBarTela(if (uiState.editando) "Editar produto" else "Novo produto", onVoltar) },
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
                valor = uiState.nome,
                onChange = { viewModel.onNomeChange(it) },
                rotulo = "Nome",
                erro = if (uiState.tentouSalvar) erros.nome else null
            )

            CampoTexto(
                valor = uiState.volume,
                onChange = { viewModel.onVolumeChange(it) },
                rotulo = "Volume (ex: 350ml)"
            )

            CampoTexto(
                valor = uiState.estabelecimento,
                onChange = { viewModel.onEstabelecimentoChange(it) },
                rotulo = "Estabelecimento",
                erro = if (uiState.tentouSalvar) erros.estabelecimento else null
            )

            CampoTexto(
                valor = uiState.precoTexto,
                onChange = { viewModel.onPrecoChange(it) },
                rotulo = "Preço (R$)",
                teclado = KeyboardType.Decimal,
                erro = if (uiState.tentouSalvar) erros.preco else null
            )

            CampoTexto(
                valor = uiState.descontoTexto,
                onChange = { viewModel.onDescontoChange(it) },
                rotulo = "Desconto em % (opcional)",
                teclado = KeyboardType.Number
            )

            CampoTexto(
                valor = uiState.descricao,
                onChange = { viewModel.onDescricaoChange(it) },
                rotulo = "Descrição",
                minLinhas = 3
            )

            Text("Categoria", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.categorias, key = { it.id }) { categoria ->
                    FilterChip(
                        selected = categoria.id == uiState.categoriaId,
                        onClick = { viewModel.onCategoriaChange(categoria.id) },
                        label = { Text(categoria.nome) }
                    )
                }
            }
            if (uiState.tentouSalvar && erros.categoria != null) {
                Text(erros.categoria, color = Color(0xFFE53935), fontSize = 12.sp)
            }

            Text("Imagem", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(IMAGENS_DISPONIVEIS) { recurso ->
                    val selecionada = recurso == uiState.imagem
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
                            .clickable { viewModel.onImagemChange(recurso) }
                            .padding(8.dp)
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = { viewModel.onSalvar { onVoltar() } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043))
            ) {
                Text(
                    text = if (uiState.editando) "Salvar alterações" else "Adicionar",
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