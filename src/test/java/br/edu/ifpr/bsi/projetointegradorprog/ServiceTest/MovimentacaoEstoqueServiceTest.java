package br.edu.ifpr.bsi.projetointegradorprog.ServiceTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.MovimentacaoEstoque;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.*;
import br.edu.ifpr.bsi.projetointegradorprog.services.MovimentacaoEstoqueService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Transactional
public class MovimentacaoEstoqueServiceTest {

    @Autowired
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

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

    private MovimentacaoEstoque montarMovimentacao(String tipo, Integer quantidade, Produto produto, Usuario usuario) {
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
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

        MovimentacaoEstoque movimentacao = montarMovimentacao("ENTRADA", 5, produto, usuario);

        MovimentacaoEstoque movimentacaoSalva = movimentacaoEstoqueService.salvar(movimentacao);
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

        MovimentacaoEstoque movimentacao = montarMovimentacao("SAIDA", 3, produto, usuario);

        MovimentacaoEstoque movimentacaoSalva = movimentacaoEstoqueService.salvar(movimentacao);
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

        MovimentacaoEstoque movimentacao = montarMovimentacao("SAIDA", 50, produto, usuario);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            movimentacaoEstoqueService.salvar(movimentacao);
        });

        Assertions.assertEquals("Estoque insuficiente para saída.", exception.getMessage());
    }

    @Test
    public void testAtualizarMovimentacao() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoEstoque movimentacao = montarMovimentacao("ENTRADA", 5, produto, usuario);
        MovimentacaoEstoque movimentacaoSalva = movimentacaoEstoqueService.salvar(movimentacao);

        MovimentacaoEstoque novaMovimentacao = new MovimentacaoEstoque();
        novaMovimentacao.setTipo("SAIDA");
        novaMovimentacao.setQuantidade(3);
        novaMovimentacao.setObservacao("Movimentação atualizada");
        novaMovimentacao.setProduto(produto);
        novaMovimentacao.setUsuario(usuario);

        MovimentacaoEstoque movimentacaoAtualizada = movimentacaoEstoqueService.atualizar(movimentacaoSalva.getId(), novaMovimentacao);
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

        MovimentacaoEstoque movimentacao = montarMovimentacao("ENTRADA", 5, produto, usuario);
        MovimentacaoEstoque movimentacaoSalva = movimentacaoEstoqueService.salvar(movimentacao);

        boolean deletou = movimentacaoEstoqueService.deletar(movimentacaoSalva.getId());
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);
        MovimentacaoEstoque movimentacaoEncontrada = movimentacaoEstoqueRepository.findById(movimentacaoSalva.getId()).orElse(null);

        Assertions.assertTrue(deletou);
        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(10, produtoAtualizado.getQuantidadeEstoque());
        Assertions.assertNull(movimentacaoEncontrada);
    }

    @Test
    public void testBuscarPorId() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoEstoque movimentacao = movimentacaoEstoqueService.salvar(montarMovimentacao("ENTRADA", 2, produto, usuario));

        MovimentacaoEstoque movimentacaoEncontrada = movimentacaoEstoqueService.buscarPorId(movimentacao.getId());

        Assertions.assertNotNull(movimentacaoEncontrada);
        Assertions.assertEquals("ENTRADA", movimentacaoEncontrada.getTipo());
    }

    @Test
    public void testListarTodos() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoEstoqueService.salvar(montarMovimentacao("ENTRADA", 1, produto, usuario));

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueService.listarTodos();

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorTipo() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoEstoqueService.salvar(montarMovimentacao("SAIDA", 2, produto, usuario));

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueService.buscarPorTipo("SAIDA");

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorQuantidadeMaiorIgual() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoEstoqueService.salvar(montarMovimentacao("ENTRADA", 8, produto, usuario));

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueService.buscarPorQuantidadeMaiorIgual(5);

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorTipoLimit() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        for (int i = 0; i < 12; i++) {
            movimentacaoEstoqueService.salvar(montarMovimentacao("ENTRADA", 1, produto, usuario));
        }

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueService.buscarPorTipoLimit("ENTRADA", 10);

        Assertions.assertFalse(movimentacoes.isEmpty());
        Assertions.assertEquals(10, movimentacoes.size());
    }
}
