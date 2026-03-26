package br.edu.ifpr.bsi.projetointegradorprog.ServiceTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.FornecedorRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.ProdutoRepository;
import br.edu.ifpr.bsi.projetointegradorprog.services.ProdutoService;
import br.edu.ifpr.bsi.projetointegradorprog.services.UsuarioService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    @Autowired
    private UsuarioService usuarioService;

    private Categoria criarCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Informática");
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

    private Produto montarProduto(Categoria categoria, Fornecedor fornecedor, String sku, String nome) {
        Produto produto = new Produto();
        produto.setSku(sku);
        produto.setNome(nome);
        produto.setDescricao("Descrição de " + nome);
        produto.setPreco(new BigDecimal("100.00"));
        produto.setQuantidadeEstoque(10);
        produto.setQuantidadeMinima(2);
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);
        return produto;
    }

    @Test
    public void testSalvar() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        Produto produto = montarProduto(categoria, fornecedor, "SKU001", "Mouse");

        Produto produtoSalvo = produtoService.salvar(produto);

        Assertions.assertNotNull(produtoSalvo);
        Assertions.assertNotNull(produtoSalvo.getId());
        Assertions.assertNotNull(produtoSalvo.getDataCadastro());
    }

    @Test
    public void testListarTodos() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        produtoService.salvar(montarProduto(categoria, fornecedor, "SKU002", "Teclado"));

        List<Produto> produtos = produtoService.listarTodos();

        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        Produto produto = produtoService.salvar(montarProduto(categoria, fornecedor, "SKU003", "Monitor"));

        Produto produtoEncontrado = produtoService.buscarPorId(produto.getId());

        Assertions.assertNotNull(produtoEncontrado);
        Assertions.assertEquals("Monitor", produtoEncontrado.getNome());
    }

    @Test
    public void testAtualizar() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        Produto produto = produtoService.salvar(montarProduto(categoria, fornecedor, "SKU004", "SSD"));

        Produto novoProduto = montarProduto(categoria, fornecedor, "SKU004A", "SSD 1TB");
        novoProduto.setQuantidadeEstoque(20);
        novoProduto.setQuantidadeMinima(5);

        Produto produtoAtualizado = produtoService.atualizar(produto.getId(), novoProduto);

        Assertions.assertNotNull(produtoAtualizado);
        Assertions.assertEquals("SSD 1TB", produtoAtualizado.getNome());
        Assertions.assertEquals("SKU004A", produtoAtualizado.getSku());
        Assertions.assertEquals(20, produtoAtualizado.getQuantidadeEstoque());
    }

    @Test
    public void testDeletar() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        Produto produto = produtoService.salvar(montarProduto(categoria, fornecedor, "SKU005", "Headset"));

        boolean deletou = produtoService.deletar(produto.getId());
        Produto produtoEncontrado = produtoRepository.findById(produto.getId()).orElse(null);

        Assertions.assertTrue(deletou);
        Assertions.assertNull(produtoEncontrado);
    }

    @Test
    public void testBuscarPorNome() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        produtoService.salvar(montarProduto(categoria, fornecedor, "SKU006", "Notebook"));

        List<Produto> produtos = produtoService.buscarPorNome("Notebook");

        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void testBuscarPorSku() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        produtoService.salvar(montarProduto(categoria, fornecedor, "SKU007", "Impressora"));

        List<Produto> produtos = produtoService.buscarPorSku("SKU007");

        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void testBuscarProdutosComEstoqueBaixo() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        Produto produto = montarProduto(categoria, fornecedor, "SKU008", "Memória RAM");
        produto.setQuantidadeEstoque(1);
        produto.setQuantidadeMinima(5);

        produtoService.salvar(produto);

        List<Produto> produtos = produtoService.buscarProdutosComEstoqueBaixo();

        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void testBuscarPorNomeLikeLimit() {
        Categoria categoria = criarCategoria();
        Fornecedor fornecedor = criarFornecedor();

        for (int i = 0; i < 12; i++) {
            produtoService.salvar(montarProduto(categoria, fornecedor, "SKU90" + i, "Mouse " + i));
        }

        List<Produto> produtos = produtoService.buscarPorNomeLikeLimit("Mouse", 10);

        Assertions.assertFalse(produtos.isEmpty());
        Assertions.assertEquals(10, produtos.size());
    }

    @Test
    public void testSalvarCategoriaInexistente() {
        Fornecedor fornecedor = criarFornecedor();

        Categoria categoriaFake = new Categoria();
        categoriaFake.setId(999L);

        Produto produto = new Produto();
        produto.setSku("SKU999");
        produto.setNome("Produto Inválido");
        produto.setDescricao("Teste");
        produto.setPreco(new BigDecimal("50.00"));
        produto.setQuantidadeEstoque(5);
        produto.setQuantidadeMinima(1);
        produto.setCategoria(categoriaFake);
        produto.setFornecedor(fornecedor);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            produtoService.salvar(produto);
        });

        Assertions.assertEquals("Categoria não encontrada.", exception.getMessage());
    }

    @Test
    public void testSalvarFornecedorInexistente() {
        Categoria categoria = criarCategoria();

        Fornecedor fornecedorFake = new Fornecedor();
        fornecedorFake.setId(999L);

        Produto produto = new Produto();
        produto.setSku("SKU998");
        produto.setNome("Produto Inválido 2");
        produto.setDescricao("Teste");
        produto.setPreco(new BigDecimal("50.00"));
        produto.setQuantidadeEstoque(5);
        produto.setQuantidadeMinima(1);
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedorFake);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            produtoService.salvar(produto);
        });

        Assertions.assertEquals("Fornecedor não encontrado.", exception.getMessage());
    }


}
