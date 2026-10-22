package com.example.myapplication.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.SportsBar
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room3.ColumnTypeConverter

class Conversores {
    private val icones: Map<String, ImageVector> = mapOf(
        "SportsBar" to Icons.Outlined.SportsBar,
        "WineBar" to Icons.Outlined.WineBar,
        "Liquor" to Icons.Outlined.Liquor,
        "AcUnit" to Icons.Outlined.AcUnit,
        "LocalDrink" to Icons.Outlined.LocalDrink
    )

    @ColumnTypeConverter
    fun iconeParaTexto(icone: ImageVector): String {
        return icones.entries.firstOrNull { it.value == icone }?.key ?: "SportsBar"
    }

    @ColumnTypeConverter
    fun textoParaIcone(nome: String): ImageVector {
        return icones[nome] ?: Icons.Outlined.SportsBar
    }

    @ColumnTypeConverter
    fun corParaInt(cor: Color): Int = cor.toArgb()

    @ColumnTypeConverter
    fun intParaCor(valor: Int): Color = Color(valor)
}