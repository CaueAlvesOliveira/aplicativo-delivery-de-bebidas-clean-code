package com.example.myapplication.ui.formCategoria

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SportsBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.data.local.entity.Categoria

data class FormCategoriaUiState(
    val nome: String = "",
    val icone: ImageVector = Icons.Outlined.SportsBar,
    val cor: Color = Color(0xFF29B6F6),
    val categoriaEmEdicao: Categoria? = null,
    val tentouSalvar: Boolean = false
) {
    val editando: Boolean
        get() = categoriaEmEdicao != null

    val erroNome: String?
        get() = if (nome.isBlank()) "Informe o nome" else null
}