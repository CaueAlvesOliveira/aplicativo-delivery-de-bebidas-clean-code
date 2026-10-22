package com.example.myapplication.navegacao

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navegacao.rotas.Rotas
import com.example.myapplication.ui.screens.TelaCarrinho
import com.example.myapplication.ui.screens.TelaFormCategoria
import com.example.myapplication.ui.screens.TelaFormProduto
import com.example.myapplication.ui.screens.TelaListaCategorias
import com.example.myapplication.ui.screens.TelaListaProdutos
import com.example.myapplication.ui.screens.TelaPagamento
import com.example.myapplication.ui.screens.TelaProduto
import com.example.myapplication.ui.screens.TelaProdutoPorCategoria
import com.example.myapplication.ui.screens.TelaRastreio
import com.example.myapplication.ui.screens.telaInicio
import com.example.myapplication.viewmodel.LojaViewModel

@Composable
fun NavagacaoEntreTela() {

    val contexto = LocalContext.current
    val navInterno = rememberNavController()

    val viewModel: LojaViewModel = viewModel() {
        LojaViewModel(contexto)
    }

    val navInicio = { navInterno.navigate(Rotas.HOME) }
    val navCarrinho = { navInterno.navigate(Rotas.CARRINHO) }
    val navPedidos = { navInterno.navigate(Rotas.RASTREIO) }
    val navProdutos = { navInterno.navigate(Rotas.PRODUTOS) }

    NavHost(
        navController = navInterno,
        startDestination = Rotas.HOME
    ) {
        composable(Rotas.HOME){ telaInicio(navInterno, viewModel)}
        composable(Rotas.PRODUTO){backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            TelaProduto(
                produtoId = id,
                viewModel = viewModel,
                onVoltar = {navInterno.popBackStack()},
                irParaCarrinho = {navInterno.navigate(Rotas.CARRINHO)},
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
        composable(Rotas.PRODUTOS){
            TelaListaProdutos(
                viewModel = viewModel,
                onVoltar = {navInterno.popBackStack()},
                onNovo = {navInterno.navigate(Rotas.formProduto())},
                onEditar = {id -> navInterno.navigate(Rotas.formProduto(id))},
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos,
                onAbrir = {id -> navInterno.navigate(Rotas.produto(id))},
            )
        }
        composable(Rotas.FORM_PRODUTO){backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            TelaFormProduto(
                produtoId = id,
                viewModel = viewModel,
                onVoltar = {navInterno.popBackStack()},
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
        composable(Rotas.CARRINHO){
            TelaCarrinho(
                viewModel = viewModel,
                onVoltar = {navInterno.popBackStack()},
                onIrParaPagamento = {navInterno.navigate(Rotas.PAGAMENTO)},
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
        composable(Rotas.PAGAMENTO){
            TelaPagamento(
                onVoltar = {navInterno.popBackStack()},
                navController = navInterno,
                viewModel = viewModel,
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
        composable(Rotas.RASTREIO){
            TelaRastreio(
                onVoltar = {navInterno.popBackStack()},
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
        composable(Rotas.CATEGORIAS) {
            TelaListaCategorias(
                viewModel = viewModel,
                onVoltar = { navInterno.popBackStack() },
                onNovo = { navInterno.navigate(Rotas.formCategoria()) },
                onEditar = { id -> navInterno.navigate(Rotas.formCategoria(id)) },
                onAbrir = { id -> navInterno.navigate(Rotas.produtosPorCategoria(id)) },
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
        composable(Rotas.FORM_CATEGORIA) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            TelaFormCategoria(
                categoriaId = id,
                viewModel = viewModel,
                onVoltar = { navInterno.popBackStack() },
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
        composable(Rotas.PRODUTOS_POR_CATEGORIA) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            TelaProdutoPorCategoria(
                categoriaId = id,
                viewModel = viewModel,
                onVoltar = { navInterno.popBackStack() },
                onProdutoClick = { produtoId -> navInterno.navigate(Rotas.produto(produtoId)) },
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
    }
}
