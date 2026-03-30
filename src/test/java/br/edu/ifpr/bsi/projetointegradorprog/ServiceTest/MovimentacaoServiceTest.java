package br.edu.ifpr.bsi.projetointegradorprog.ServiceTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.Movimentacao;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.*;
import br.edu.ifpr.bsi.projetointegradorprog.services.MovimentacaoService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Transactional
public class MovimentacaoServiceTest {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Produto criarProduto() {
        Categoria categoria = new Categoria();
        categoria.setNome("Informática");
        categoria.setDescricao("Categoria teste");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT200");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("22345678000100");
        fornecedor.setEmail("fornecedor@email.com");
        fornecedor.setTelefone("46988888888");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU200");
        produto.setNome("Mouse Gamer");
        produto.setDescricao("Produto teste");
        produto.setPreco(new BigDecimal("150.00"));
        produto.setQuantidadeEstoque(10);
        produto.setQuantidadeMinima(2);
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        return produtoRepository.save(produto);
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");
        usuario.setPerfil("ADMIN");
        usuario.setMatricula("MAT200");
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }

    private Movimentacao montarMovimentacao(String tipo, Integer quantidade, Produto produto, Usuario usuario) {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipo(tipo);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setObservacao("Movimentação de teste");
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);
        return movimentacao;
    }

    @Test
    public void testSalvarEntrada() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        Movimentacao movimentacao = montarMovimentacao("ENTRADA", 5, produto, usuario);

        Movimentacao movimentacaoSalva = movimentacaoService.salvar(movimentacao);
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);

        Assertions.assertNotNull(movimentacaoSalva);
        Assertions.assertEquals(10, movimentacaoSalva.getSaldoAnterior());
        Assertions.assertEquals(15, movimentacaoSalva.getSaldoAtual());
        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(15, produtoAtualizado.getQuantidadeEstoque());
    }

    @Test
    public void testSalvarSaida() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        Movimentacao movimentacao = montarMovimentacao("SAIDA", 3, produto, usuario);

        Movimentacao movimentacaoSalva = movimentacaoService.salvar(movimentacao);
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);

        Assertions.assertNotNull(movimentacaoSalva);
        Assertions.assertEquals(10, movimentacaoSalva.getSaldoAnterior());
        Assertions.assertEquals(7, movimentacaoSalva.getSaldoAtual());
        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(7, produtoAtualizado.getQuantidadeEstoque());
    }

    @Test
    public void testSalvarSaidaComEstoqueInsuficiente() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        Movimentacao movimentacao = montarMovimentacao("SAIDA", 50, produto, usuario);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            movimentacaoService.salvar(movimentacao);
        });

        Assertions.assertEquals("Estoque insuficiente para saída.", exception.getMessage());
    }

    @Test
    public void testAtualizarMovimentacao() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        Movimentacao movimentacao = montarMovimentacao("ENTRADA", 5, produto, usuario);
        Movimentacao movimentacaoSalva = movimentacaoService.salvar(movimentacao);

        Movimentacao novaMovimentacao = new Movimentacao();
        novaMovimentacao.setTipo("SAIDA");
        novaMovimentacao.setQuantidade(3);
        novaMovimentacao.setObservacao("Movimentação atualizada");
        novaMovimentacao.setProduto(produto);
        novaMovimentacao.setUsuario(usuario);

        Movimentacao movimentacaoAtualizada = movimentacaoService.atualizar(movimentacaoSalva.getId(), novaMovimentacao);
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);

        Assertions.assertNotNull(movimentacaoAtualizada);
        Assertions.assertEquals("SAIDA", movimentacaoAtualizada.getTipo());
        Assertions.assertEquals(10, movimentacaoAtualizada.getSaldoAnterior());
        Assertions.assertEquals(7, movimentacaoAtualizada.getSaldoAtual());
        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(7, produtoAtualizado.getQuantidadeEstoque());
    }

    @Test
    public void testDeletarMovimentacaoRevertendoEstoque() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        Movimentacao movimentacao = montarMovimentacao("ENTRADA", 5, produto, usuario);
        Movimentacao movimentacaoSalva = movimentacaoService.salvar(movimentacao);

        boolean deletou = movimentacaoService.deletar(movimentacaoSalva.getId());
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);
        Movimentacao movimentacaoEncontrada = movimentacaoRepository.findById(movimentacaoSalva.getId()).orElse(null);

        Assertions.assertTrue(deletou);
        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(10, produtoAtualizado.getQuantidadeEstoque());
        Assertions.assertNull(movimentacaoEncontrada);
    }

    @Test
    public void testBuscarPorId() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        Movimentacao movimentacao = movimentacaoService.salvar(montarMovimentacao("ENTRADA", 2, produto, usuario));

        Movimentacao movimentacaoEncontrada = movimentacaoService.buscarPorId(movimentacao.getId());

        Assertions.assertNotNull(movimentacaoEncontrada);
        Assertions.assertEquals("ENTRADA", movimentacaoEncontrada.getTipo());
    }

    @Test
    public void testListarTodos() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoService.salvar(montarMovimentacao("ENTRADA", 1, produto, usuario));

        List<Movimentacao> movimentacoes = movimentacaoService.listarTodos();

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorTipo() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoService.salvar(montarMovimentacao("SAIDA", 2, produto, usuario));

        List<Movimentacao> movimentacoes = movimentacaoService.buscarPorTipo("SAIDA");

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorQuantidadeMaiorIgual() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoService.salvar(montarMovimentacao("ENTRADA", 8, produto, usuario));

        List<Movimentacao> movimentacoes = movimentacaoService.buscarPorQuantidadeMaiorIgual(5);

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorTipoLimit() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        for (int i = 0; i < 12; i++) {
            movimentacaoService.salvar(montarMovimentacao("ENTRADA", 1, produto, usuario));
        }

        List<Movimentacao> movimentacoes = movimentacaoService.buscarPorTipoLimit("ENTRADA", 10);

        Assertions.assertFalse(movimentacoes.isEmpty());
        Assertions.assertEquals(10, movimentacoes.size());
    }
}
