package com.example.myapplication.ui.screens.telaFormDeCategoria

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.ui.components.BarraDeNavegacaoInferior
import com.example.myapplication.ui.components.TopBarTela
import com.example.myapplication.ui.screens.telaDeInicio.ItemCategoria
import com.example.myapplication.viewmodel.LojaViewModel

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
    viewModel: LojaViewModel,
    onVoltar: () -> Unit,
    onInicio: () -> Unit = {},
    onCarrinho: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onProdutos: () -> Unit = {}
) {
    val existente = viewModel.buscarCategoria(categoriaId)
    val editando = existente != null

    var nome by rememberSaveable { mutableStateOf(existente?.nome ?: "") }
    var indiceIcone by rememberSaveable {
        mutableIntStateOf(
            existente?.let { ICONES_DISPONIVEIS.indexOf(it.icone) }?.coerceAtLeast(0) ?: 0
        )
    }
    val icone = ICONES_DISPONIVEIS[indiceIcone]
    var indiceCor by rememberSaveable {
        mutableIntStateOf(
            existente?.let { CORES_DISPONIVEIS.indexOf(it.cor) }?.coerceAtLeast(0) ?: 0
        )
    }
    var tentouSalvar by rememberSaveable { mutableStateOf(false) }

    val corSelecionada = CORES_DISPONIVEIS[indiceCor]

    val erroNome = viewModel.validarNomeCategoria(nome)

    fun salvar() {
        tentouSalvar = true
        if (viewModel.salvarCategoria(existente?.id, nome, icone, corSelecionada)) {
            onVoltar()
        }
    }

    Scaffold(
        topBar = { TopBarTela(if (editando) "Editar categoria" else "Nova categoria", onVoltar) },
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
                ItemCategoria(
                    Categoria(
                        id = 0,
                        nome = nome.ifBlank { "Nome" },
                        icone = icone,
                        cor = corSelecionada
                    )
                )
            }

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                isError = tentouSalvar && erroNome != null,
                supportingText = if (tentouSalvar && erroNome != null) {
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
                itemsIndexed(ICONES_DISPONIVEIS) { indice, vetor ->
                    val selecionado = indice == indiceIcone
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(
                                width = if (selecionado) 3.dp else 1.dp,
                                color = if (selecionado) Color(0xFFFF7043) else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { indiceIcone = indice },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = vetor,
                            contentDescription = "Opção de ícone",
                            tint = corSelecionada,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Text("Cor", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(CORES_DISPONIVEIS) { indice, cor ->
                    val selecionada = indice == indiceCor
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(cor, CircleShape)
                            .border(
                                width = if (selecionada) 3.dp else 1.dp,
                                color = if (selecionada) Color.Black else Color.LightGray,
                                shape = CircleShape
                            )
                            .clickable { indiceCor = indice }
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