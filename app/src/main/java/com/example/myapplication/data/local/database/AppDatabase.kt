package com.example.myapplication.data.local.database

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.myapplication.data.local.dao.CarrinhoDAO
import com.example.myapplication.data.local.dao.CategoriaDAO
import com.example.myapplication.data.local.dao.ProdutoDAO
import com.example.myapplication.data.local.entity.Categoria
import com.example.myapplication.data.local.entity.ItemCarrinho
import com.example.myapplication.data.local.entity.Produto

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