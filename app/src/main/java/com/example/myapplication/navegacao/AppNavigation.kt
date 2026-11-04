package com.example.myapplication.navegacao

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.navegacao.rotas.Rotas
import com.example.myapplication.ui.carrinho.TelaCarrinho
import com.example.myapplication.ui.formCategoria.TelaFormCategoria
import com.example.myapplication.ui.formProduto.TelaFormProduto
import com.example.myapplication.ui.listaCategorias.TelaListaCategorias
import com.example.myapplication.ui.listaProdutos.TelaListaProdutos
import com.example.myapplication.ui.pagamento.TelaPagamento
import com.example.myapplication.ui.produto.TelaProduto
import com.example.myapplication.ui.produtosPorCategoria.TelaProdutoPorCategoria
import com.example.myapplication.ui.rastreio.TelaRastreio
import com.example.myapplication.ui.inicio.TelaInicio

@Composable
fun NavegacaoEntreTela() {

    val contexto = LocalContext.current
    val navInterno = rememberNavController()

    val navInicio = { navInterno.navigate(Rotas.HOME) }
    val navCarrinho = { navInterno.navigate(Rotas.CARRINHO) }
    val navPedidos = { navInterno.navigate(Rotas.RASTREIO) }
    val navProdutos = { navInterno.navigate(Rotas.PRODUTOS) }

    NavHost(
        navController = navInterno,
        startDestination = Rotas.HOME
    ) {
        composable(Rotas.HOME){ TelaInicio(navInterno)}
        composable(Rotas.PRODUTO){backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            TelaProduto(
                produtoId = id,
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
                onVoltar = {navInterno.popBackStack()},
                onInicio = navInicio,
                onCarrinho = navCarrinho,
                onPedidos = navPedidos,
                onProdutos = navProdutos
            )
        }
        composable(Rotas.CARRINHO){
            TelaCarrinho(
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
