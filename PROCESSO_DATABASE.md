# Registro do processo da etapa 3 do trabalho (banco de dados)

Nessa etapa, vamos juntar as features que já existem no código com o banco de dados, até então os objetos eram mantidos apenas em memória e desapareciam quando a aplicação era encerrada. Agora o objetivo é manter os dados no banco de dados, utilizando o Room.

## Entidades
A primeira coisa que fazemos é transformar as classes dos produtos e categorias em entidades para as tabelas do banco:

![entidade de produto](anexos/processo/prints/entidade_produto.png)

![entidade de categoria](anexos/processo/prints/entidade_categoria.png)

- Categoria: `id` (chave primária com `autoGenerate`), `nome`, `icone` e `cor`. Esses dois últimos são usados para desenhar a categoria na tela inicial e na lista de categorias, por isso foram mantidos na entidade.
- Produto: `id` (chave primária com `autoGenerate`), `nome`, `volume`, `estabelecimento`, `preco`, `imagem`, `desconto` (opcional, só existe nos produtos em oferta), `descricao` e `categoriaId`, que liga o produto à sua categoria.

## Database

Em seguida criamos o AppDatabase, para retornar a conexão com o banco. Ele segue o padrão Singleton, reúne as entidades Categoria, Produto e ItemCarrinho e expõe os três DAOs (produtoDAO(), categoriaDAO() e carrinhoDAO()). A versão do banco está em 1, porque adicionamos o carrinho posteriormente.

![print do AppDatabase](anexos/processo/prints/AppDatabase.png)

## DAOs e consultas

Cada entidade tem o seu DAO, e todos os métodos são suspend:

ProdutoDAO: os quatro métodos básicos (inserir, atualizar, deletar e buscarProdutos), além de buscarProdutoPorId, buscarProdutosPorCategoria, deletarPorId e deletarPorCategoria.
CategoriaDAO: os quatro métodos básicos (inserir, atualizar, deletar e buscarCategorias), além de buscarCategoriaPorId e deletarPorId.
CarrinhoDAO: salvar (um @Upsert, que insere o item ou atualiza a quantidade se ele já estiver no carrinho), buscarItens, deletarPorProduto e limpar.

As consultas extras foram criadas para as necessidades das telas: buscarProdutoPorId e buscarCategoriaPorId alimentam as telas que abrem um item específico, buscarProdutosPorCategoria e deletarPorCategoria existem porque o produto guarda o categoriaId, e as consultas do carrinho existem porque cada linha do carrinho é identificada pelo produto.

## Escolhas de arquitetura

Decidimos por manter uma arquitetura que funciona da seguinte maneira: as listas que antes guardavam os objetos agora funcionam como uma espécie de cache, ou seja, um espelho do próprio banco de dados. Quando iniciamos a aplicação, uma função popula o banco com os dados pré-criados, mas só se ele estiver vazio (para não recriar itens que o usuário removeu nem sobrescrever o que ele editou), e em seguida carrega as listas a partir do banco. Depois disso, toda alteração (adicionar, editar ou remover) é gravada primeiro no banco e então uma função recarrega as listas a partir dele, de modo que a tela sempre mostra o que está salvo.

Além disso, criamos um repository (LojaRepository) que concentra todas as operações diretamente nos DAOs, assim a ViewModel só conhece o repositório e não chama os métodos dos DAOs diretamente. As operações de banco rodam em Dispatchers.IO e são envolvidas em try/catch com Log.e, para não travar a interface e para registrar os erros.

![print da funçõa que povoa o banco de dados](anexos/processo/prints/dados-iniciais.png)

![print da função que atualiza o banco de dados](anexos/processo/prints/atualiza-banco.png)

![print do repository](anexos/processo/prints/LojaRepository.png)

## Telas de detalhes lendo do banco

A tela de produto e a tela de produtos por categoria não recebem o objeto pronto: elas recebem apenas o id pela navegação e buscam o item direto no banco dentro de um LaunchedEffect.

## Dificuldade: tipos que o Room não consegue gravar

No entanto, quando rodamos a aplicação ela deu erro, aparentemente o Room só consegue gravar tipos simples, então quando tentamos gravar no banco imagens e cores ele falha:

![build com erros](anexos/processo/prints/build_com_erros.png)

Para contornar essa dificuldade criamos um arquivo de conversores (Conversores), registrado no AppDatabase, que converte esses atributos que o Room não consegue lidar bem para tipagens mais simples que funcionam: o ícone é gravado como um texto com o nome dele e a cor é gravada como um número inteiro.

![print do arquivo com os conversores](anexos/processo/prints/Conversores.png)

## Carrinho de compras

Por fim, decidimos criar o carrinho de compras como uma terceira entidade, salva no banco junto com os produtos e categorias. Cada linha guarda o produtoId (que é a chave primária e uma chave estrangeira para o produto) e a quantidade. Assim, quando saímos do app o carrinho se mantém até que seja finalizada a compra, momento em que ele é limpo. Se um produto é removido, o item dele também é removido do carrinho.

![print do item do carrinho](anexos/processo/prints/entidade_itemCarrinho.png)

![DAO do carrinho](anexos/processo/prints/carrinhoDAO.png)

## Persistência

Assim os produtos, categorias e carrinho de compras ficam salvos no banco de dados, e todas as alterações que são realizadas neles se mantêm mesmo depois que fechamos o aplicativo, a seguir segue o vídeo de demonstração do aplicativo funcionando.

[![video de demonstração](https://drive.google.com/file/d/1aVu5buBrhNke0V0mbe0LcK8vXrRCmsMb/view?usp=sharing)](https://drive.google.com/file/d/1aVu5buBrhNke0V0mbe0LcK8vXrRCmsMb/view?usp=sharing)

## Dificuldades da equipe

Nossa maior dificuldade foi ajustar as tipagens dos atributos para que pudessem ser salvas no banco de dados, além disso foi díficil entender como funcionaria o fluxo de execução dos metódos do banco, se deveríamos passar o contexto do aplicativo diretamente como parâmetro para o ViewModel ou de outra maneira. Por fim decidimos passar o contexto ao ViewModel e o AppDatabase para o repositório que chama os metódos dos Daos, assim o ViewModel não conhece diretamente o banco e apenas consome esse recurso gerenciado por outro arquivo.


