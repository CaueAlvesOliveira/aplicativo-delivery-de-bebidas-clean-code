package com.example.myapplication.data

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.myapplication.data.dao.CarrinhoDAO
import com.example.myapplication.data.dao.CategoriaDAO
import com.example.myapplication.data.dao.ProdutoDAO
import com.example.myapplication.model.Categoria
import com.example.myapplication.model.ItemCarrinho
import com.example.myapplication.model.Produto

@Database(entities = [Categoria::class, Produto::class, ItemCarrinho::class], version = 2)
@ColumnTypeConverters(Conversores::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDAO(): ProdutoDAO
    abstract fun categoriaDAO(): CategoriaDAO

    abstract fun carrinhoDAO(): CarrinhoDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            } else {
                synchronized(this){

                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()

                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
}