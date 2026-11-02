package com.example.myapplication.ui.formCategoria

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.SportsBar
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.repository.CategoriaRepository
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela
import com.example.myapplication.ui.inicio.ItemCategoria

private val ICONES_DISPONIVEIS = listOf(
    Icons.Outlined.SportsBar,
    Icons.Outlined.WineBar,
    Icons.Outlined.Liquor,
    Icons.Outlined.AcUnit,
    Icons.Outlined.LocalDrink
)

private val CORES_DISPONIVEIS = listOf(
    Color(0xFF29B6F6),
    Color(0xFF7E57C2),
    Color(0xFFFF7043),
    Color(0xFF66BB6A),
    Color(0xFF8D6E63),
    Color(0xFFEC407A),
    Color(0xFFFFCA28),
    Color(0xFF26A69A),
    Color(0xFFEF5350)
)

@Composable
fun TelaFormCategoria(
    categoriaId: Int,
    onVoltar: () -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {},
    viewModel: FormCategoriaViewModel = viewModel(
        factory = FormCategoriaViewModelFactory(
            categoriaId,
            CategoriaRepository(AppDatabase.getDatabase(LocalContext.current).categoriaDAO())
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val erroNome = if (uiState.tentouSalvar) uiState.erroNome else null

    Scaffold(
        topBar = { TopBarTela(if (uiState.editando) "Editar categoria" else "Nova categoria", onVoltar) },
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CategoriaPrevia(
                    Categoria(
                        id = 0,
                        nome = uiState.nome.ifBlank { "Nome" },
                        icone = uiState.icone,
                        cor = uiState.cor
                    )
                )
            }

            OutlinedTextField(
                value = uiState.nome,
                onValueChange = viewModel::onNomeChange,
                label = { Text("Nome") },
                isError = erroNome != null,
                supportingText = if (erroNome != null) {
                    { Text(erroNome) }
                } else null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.DarkGray,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Text("Ícone", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ICONES_DISPONIVEIS) { vetor ->
                    val selecionado = vetor == uiState.icone
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(
                                width = if (selecionado) 3.dp else 1.dp,
                                color = if (selecionado) Color(0xFFFF7043) else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.onIconeChange(vetor) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = vetor,
                            contentDescription = "Opção de ícone",
                            tint = uiState.cor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Text("Cor", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(CORES_DISPONIVEIS) { cor ->
                    val selecionada = cor == uiState.cor
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(cor, CircleShape)
                            .border(
                                width = if (selecionada) 3.dp else 1.dp,
                                color = if (selecionada) Color.Black else Color.LightGray,
                                shape = CircleShape
                            )
                            .clickable { viewModel.onCorChange(cor) }
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
fun CategoriaPrevia(categoria: Categoria, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Box(
            modifier = Modifier
                .drawBehind(
                ){
                    drawCircle(
                        color = categoria.cor,
                        style = Stroke(
                            width = 5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f))
                        )
                    )
                }
        ) {
            IconButton(
                onClick = {onClick()},
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(255,255,255), shape = CircleShape)
            ) {
                Icon(
                    imageVector = categoria.icone,
                    contentDescription = "Icone categoria",
                    tint = categoria.cor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = categoria.nome,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = Color(164, 158, 160)
        )
    }
}