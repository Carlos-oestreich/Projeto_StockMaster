package br.edu.ifpr.bsi.StockMaster.ServiceTest;

import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.FornecedorRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.ProdutoRepository;
import br.edu.ifpr.bsi.StockMaster.services.ProdutoService;
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
public class ProdutoServiceTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private Categoria criarCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Informatica");
        categoria.setDescricao("Categoria teste");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT100");
        categoria.setAtivo(true);
        return categoriaRepository.save(categoria);
    }

    private Fornecedor criarFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12345678000100");
        fornecedor.setEmail("fornecedor@email.com");
        fornecedor.setTelefone("46999999999");
        fornecedor.setAtivo(true);
        return fornecedorRepository.save(fornecedor);
    }

    private ProdutoRequestDTO montarProduto(Categoria categoria, Fornecedor fornecedor, String sku, String nome) {
        return new ProdutoRequestDTO(
                sku,
                nome,
                "Descricao de " + nome,
                new BigDecimal("100.00"),
                "Marca Teste",
                10,
                2,
                null,
                categoria.getId(),
                fornecedor.getId()
        );
    }

    @Test
    public void testSalvar() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        ProdutoDetailDTO produtoSalvo = produtoService.salvar(montarProduto(categoria, fornecedor, "SKU001", "Mouse"));

        Assertions.assertNotNull(produtoSalvo);
        Assertions.assertNotNull(produtoSalvo.id());
        Assertions.assertNotNull(produtoSalvo.dataCadastro());
    }

    @Test
    public void testListarTodos() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        produtoService.salvar(montarProduto(categoria, fornecedor, "SKU002", "Teclado"));

        List<ProdutoSummaryDTO> produtos = produtoService.listarTodos();

        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        ProdutoDetailDTO produto = produtoService.salvar(montarProduto(categoria, fornecedor, "SKU003", "Monitor"));

        ProdutoDetailDTO produtoEncontrado = produtoService.buscarPorId(produto.id());

        Assertions.assertNotNull(produtoEncontrado);
        Assertions.assertEquals("Monitor", produtoEncontrado.nome());
    }

    @Test
    public void testAtualizar() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        ProdutoDetailDTO produto = produtoService.salvar(montarProduto(categoria, fornecedor, "SKU004", "SSD"));

        ProdutoDetailDTO produtoAtualizado = produtoService.atualizar(
                produto.id(),
                new ProdutoRequestDTO(
                        "SKU004A",
                        "SSD 1TB",
                        "Descricao de SSD 1TB",
                        new BigDecimal("100.00"),
                        "Marca Teste",
                        20,
                        5,
                        null,
                        categoria.getId(),
                        fornecedor.getId()
                )
        );

        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals("SSD 1TB", produtoAtualizado.nome());
        Assertions.assertEquals("SKU004A", produtoAtualizado.sku());
        Assertions.assertEquals(20, produtoAtualizado.quantidadeEstoque());
    }

    @Test
    public void testDeletar() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        ProdutoDetailDTO produto = produtoService.salvar(montarProduto(categoria, fornecedor, "SKU005", "Headset"));

        produtoService.deletar(produto.id());
        var produtoEncontrado = produtoRepository.findById(produto.id()).orElse(null);

        Assertions.assertNull(produtoEncontrado);
    }

    @Test
    public void testBuscarPorNome() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        produtoService.salvar(montarProduto(categoria, fornecedor, "SKU006", "Notebook"));

        List<ProdutoSummaryDTO> produtos = produtoService.buscarPorNome("Notebook");

        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void testBuscarPorSku() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        produtoService.salvar(montarProduto(categoria, fornecedor, "SKU007", "Impressora"));

        ProdutoSummaryDTO produtos = produtoService.buscarPorSku("SKU007");

        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void testBuscarProdutosComEstoqueBaixo() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        ProdutoRequestDTO produto = new ProdutoRequestDTO(
                "SKU008",
                "Memoria RAM",
                "Descricao de Memoria RAM",
                new BigDecimal("100.00"),
                "Marca Teste",
                1,
                5,
                null,
                categoria.getId(),
                fornecedor.getId()
        );

        produtoService.salvar(produto);

        List<ProdutoSummaryDTO> produtos = produtoService.buscarProdutosComEstoqueBaixo();

        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void testBuscarPorNomeLikeLimit() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        for (int i = 0; i < 12; i++) {
            produtoService.salvar(montarProduto(categoria, fornecedor, "SKU90" + i, "Mouse " + i));
        }

        List<ProdutoSummaryDTO> produtos = produtoService.buscarPorNomeLikeLimit("Mouse", 10);

        Assertions.assertFalse(produtos.isEmpty());
        Assertions.assertEquals(10, produtos.size());
    }

    @Test
    public void testSalvarCategoriaInexistente() {
        Fornecedor fornecedor = criarFornecedor();

        ProdutoRequestDTO produto = new ProdutoRequestDTO(
                "SKU999",
                "Produto Invalido",
                "Teste",
                new BigDecimal("50.00"),
                "Marca Teste",
                5,
                1,
                null,
                999L,
                fornecedor.getId()
        );

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () ->
                produtoService.salvar(produto)
        );

        Assertions.assertEquals("Categoria nao encontrada.", exception.getReason());
    }

    @Test
    public void testSalvarFornecedorInexistente() {
        Categoria categoria = criarCategoria();

        ProdutoRequestDTO produto = new ProdutoRequestDTO(
                "SKU998",
                "Produto Invalido 2",
                "Teste",
                new BigDecimal("50.00"),
                "Marca Teste",
                5,
                1,
                null,
                categoria.getId(),
                999L
        );

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () ->
                produtoService.salvar(produto)
        );

        Assertions.assertEquals("Fornecedor nao encontrado.", exception.getReason());
    }
}
