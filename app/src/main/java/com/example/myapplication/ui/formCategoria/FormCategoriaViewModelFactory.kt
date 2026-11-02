package com.example.myapplication.ui.formCategoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CategoriaRepository

class FormCategoriaViewModelFactory(
    private val categoriaId: Int,
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormCategoriaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormCategoriaViewModel(categoriaId, categoriaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}