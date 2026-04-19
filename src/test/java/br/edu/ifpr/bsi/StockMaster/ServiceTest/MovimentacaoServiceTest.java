package br.edu.ifpr.bsi.StockMaster.ServiceTest;

import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.FornecedorRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.MovimentacaoRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.ProdutoRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.UsuarioRepository;
import br.edu.ifpr.bsi.StockMaster.services.MovimentacaoService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

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
        categoria.setNome("Informatica");
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

    private MovimentacaoRequestDTO montarMovimentacao(String tipo, Integer quantidade, Produto produto, Usuario usuario) {
        return new MovimentacaoRequestDTO(
                tipo,
                quantidade,
                "Movimentacao de teste",
                null,
                produto.getId(),
                usuario.getId()
        );
    }

    @Test
    public void testSalvarEntrada() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoDetailDTO movimentacaoSalva = movimentacaoService.salvar(
                montarMovimentacao("ENTRADA", 5, produto, usuario)
        );
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);

        Assertions.assertNotNull(movimentacaoSalva);
        Assertions.assertEquals(10, movimentacaoSalva.saldoAnterior());
        Assertions.assertEquals(15, movimentacaoSalva.saldoAtual());
        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(15, produtoAtualizado.getQuantidadeEstoque());
    }

    @Test
    public void testSalvarSaida() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoDetailDTO movimentacaoSalva = movimentacaoService.salvar(
                montarMovimentacao("SAIDA", 3, produto, usuario)
        );
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);

        Assertions.assertNotNull(movimentacaoSalva);
        Assertions.assertEquals(10, movimentacaoSalva.saldoAnterior());
        Assertions.assertEquals(7, movimentacaoSalva.saldoAtual());
        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(7, produtoAtualizado.getQuantidadeEstoque());
    }

    @Test
    public void testSalvarSaidaComEstoqueInsuficiente() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () ->
                movimentacaoService.salvar(montarMovimentacao("SAIDA", 50, produto, usuario))
        );

        Assertions.assertEquals("Quantidade de estoque insuficiente", exception.getReason());
    }

    @Test
    public void testAtualizarMovimentacao() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoDetailDTO movimentacaoSalva = movimentacaoService.salvar(
                montarMovimentacao("ENTRADA", 5, produto, usuario)
        );

        MovimentacaoDetailDTO movimentacaoAtualizada = movimentacaoService.atualizar(
                movimentacaoSalva.id(),
                new MovimentacaoRequestDTO(
                        "SAIDA",
                        3,
                        "Movimentacao atualizada",
                        null,
                        produto.getId(),
                        usuario.getId()
                )
        );
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);

        Assertions.assertNotNull(movimentacaoAtualizada);
        Assertions.assertEquals("SAIDA", movimentacaoAtualizada.tipo());
        Assertions.assertEquals(10, movimentacaoAtualizada.saldoAnterior());
        Assertions.assertEquals(7, movimentacaoAtualizada.saldoAtual());
        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(7, produtoAtualizado.getQuantidadeEstoque());
    }

    @Test
    public void testDeletarMovimentacaoRevertendoEstoque() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoDetailDTO movimentacaoSalva = movimentacaoService.salvar(
                montarMovimentacao("ENTRADA", 5, produto, usuario)
        );

        movimentacaoService.deletar(movimentacaoSalva.id());
        Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElse(null);
        var movimentacaoEncontrada = movimentacaoRepository.findById(movimentacaoSalva.id()).orElse(null);

        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals(10, produtoAtualizado.getQuantidadeEstoque());
        Assertions.assertNull(movimentacaoEncontrada);
    }

    @Test
    public void testBuscarPorId() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoDetailDTO movimentacao = movimentacaoService.salvar(
                montarMovimentacao("ENTRADA", 2, produto, usuario)
        );

        MovimentacaoDetailDTO movimentacaoEncontrada = movimentacaoService.buscarPorId(movimentacao.id());

        Assertions.assertNotNull(movimentacaoEncontrada);
        Assertions.assertEquals("ENTRADA", movimentacaoEncontrada.tipo());
    }

    @Test
    public void testListarTodos() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoService.salvar(montarMovimentacao("ENTRADA", 1, produto, usuario));

        List<MovimentacaoSummaryDTO> movimentacoes = movimentacaoService.listarTodos();

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorTipo() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoService.salvar(montarMovimentacao("SAIDA", 2, produto, usuario));

        List<MovimentacaoSummaryDTO> movimentacoes = movimentacaoService.buscarPorTipo("SAIDA");

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorQuantidadeMaiorIgual() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        movimentacaoService.salvar(montarMovimentacao("ENTRADA", 8, produto, usuario));

        List<MovimentacaoSummaryDTO> movimentacoes = movimentacaoService.buscarPorQuantidadeMaiorIgual(5);

        Assertions.assertFalse(movimentacoes.isEmpty());
    }

    @Test
    public void testBuscarPorTipoLimit() {
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        for (int i = 0; i < 12; i++) {
            movimentacaoService.salvar(montarMovimentacao("ENTRADA", 1, produto, usuario));
        }

        List<MovimentacaoSummaryDTO> movimentacoes = movimentacaoService.buscarPorTipoLimit("ENTRADA", 10);

        Assertions.assertFalse(movimentacoes.isEmpty());
        Assertions.assertEquals(10, movimentacoes.size());
    }
}
