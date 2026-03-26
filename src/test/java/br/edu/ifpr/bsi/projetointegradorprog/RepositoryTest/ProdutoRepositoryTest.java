package br.edu.ifpr.bsi.projetointegradorprog.RepositoryTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.FornecedorRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    public void testInsert(){
        Categoria categoria = new Categoria();
        categoria.setNome("Informatica");
        categoria.setDescricao("Produtos de informatica");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT100");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Tech");
        fornecedor.setCnpj("72345678000100");
        fornecedor.setEmail("tech@gmail.com");
        fornecedor.setTelefone("46933333333");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU001");
        produto.setNome("Mouse Gamer");
        produto.setDescricao("Mouse com rgb");
        produto.setMarca("Logi");
        produto.setPreco(new BigDecimal("150.00"));
        produto.setQuantidadeEstoque(20);
        produto.setQuantidadeMinima(5);
        produto.setDataCadastro(LocalDateTime.now());
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        Produto produtoInserido = produtoRepository.save(produto);

        Produto produtoEncontrado = produtoRepository.findById(produtoInserido.getId()).orElse(null);
        Assertions.assertNotNull(produtoEncontrado, "O produto nao foi inserido");

    }

    @Test
    public void testUpdate(){
        Categoria categoria = new Categoria();
        categoria.setNome("Perifericos");
        categoria.setDescricao("Perifericos diversos");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT101");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor PC");
        fornecedor.setCnpj("8345678000100");
        fornecedor.setEmail("pc@gmail.com");
        fornecedor.setTelefone("46922222222");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU002");
        produto.setNome("Teclado");
        produto.setDescricao("Teclado mecanico");
        produto.setMarca("Red");
        produto.setPreco(new BigDecimal("250.00"));
        produto.setQuantidadeEstoque(15);
        produto.setQuantidadeMinima(3);
        produto.setDataCadastro(LocalDateTime.now());
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        Produto produtoAlterar = produtoRepository.save(produto);
        produtoAlterar.setNome("Teclado Mecanico RGB");

        Produto produtoAlterado = produtoRepository.save(produtoAlterar);

        Produto produtoEncontrado = produtoRepository.findById(produtoAlterado.getId()).orElse(null);
        Assertions.assertEquals("Teclado Mecanico RGB", produtoAlterado.getNome(), "O produto nao foi atualizado.");

    }

    @Test
    public void testDelete(){
        Categoria categoria = new Categoria();
        categoria.setNome("Acessorios");
        categoria.setDescricao("Acessorios diversos");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT102");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Acess");
        fornecedor.setCnpj("9345678000100");
        fornecedor.setEmail("acess@gmail.com");
        fornecedor.setTelefone("46911111111");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU003");
        produto.setNome("Headset");
        produto.setDescricao("Headset estereo");
        produto.setMarca("Sound");
        produto.setPreco(new BigDecimal("300.00"));
        produto.setQuantidadeEstoque(10);
        produto.setQuantidadeMinima(2);
        produto.setDataCadastro(LocalDateTime.now());
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        Produto produtoDeletar =  produtoRepository.save(produto);
        produtoRepository.delete(produtoDeletar);

        Produto produtoDeletado = produtoRepository.findById(produtoDeletar.getId()).orElse(null);
        Assertions.assertNull(produtoDeletado, "Produto nao foi deletado");

    }

    @Test
    public void testListar(){
        Categoria categoria = new Categoria();
        categoria.setNome("Monitores");
        categoria.setDescricao("Monitores diversos");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT103");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Monitor");
        fornecedor.setCnpj("10345678000100");
        fornecedor.setEmail("monitor@gmail.com");
        fornecedor.setTelefone("46900000000");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU004");
        produto.setNome("Monitor 24");
        produto.setDescricao("Monitor full hd");
        produto.setMarca("Vision");
        produto.setPreco(new BigDecimal("900.00"));
        produto.setQuantidadeEstoque(8);
        produto.setQuantidadeMinima(2);
        produto.setDataCadastro(LocalDateTime.now());
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        produtoRepository.save(produto);

        long inicio = System.currentTimeMillis();
        List<Produto> produtos = produtoRepository.findAll();
        long fim = System.currentTimeMillis();

        Assertions.assertFalse(produtos.isEmpty(), "Produtos nao encontrado.");
        Assertions.assertTrue((fim - inicio) < 300, "A consulta demorou mais de 0,3 segundos");

    }

    @Test
    public void testFindByNome(){
        Categoria categoria = new Categoria();
        categoria.setNome("Notebooks");
        categoria.setDescricao("Notebooks diversos");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT104");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor notebooks");
        fornecedor.setCnpj("11345678000100");
        fornecedor.setEmail("notebooks@gmail.com");
        fornecedor.setTelefone("46899999999");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU005");
        produto.setNome("Notebook i5");
        produto.setDescricao("Notebook 16gb");
        produto.setMarca("Fast");
        produto.setPreco(new BigDecimal("3500.00"));
        produto.setQuantidadeEstoque(5);
        produto.setQuantidadeMinima(1);
        produto.setDataCadastro(LocalDateTime.now());
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        produtoRepository.save(produto);

        List<Produto> produtos = produtoRepository.findByNome("Notebook i5");
        Assertions.assertFalse(produtos.isEmpty(), "Produtos nao encontrado.");

    }

    @Test
    public void testFindBySku(){
        Categoria categoria = new Categoria();
        categoria.setNome("Hardware");
        categoria.setDescricao("Pecas de hardware");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT105");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor hardware");
        fornecedor.setCnpj("12345678000100");
        fornecedor.setEmail("hardware@gmail.com");
        fornecedor.setTelefone("46888888888");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU006");
        produto.setNome("SSD NVME 1TB");
        produto.setDescricao("SSD NVME 1TB");
        produto.setMarca("Samsung");
        produto.setPreco(new BigDecimal("450.00"));
        produto.setQuantidadeEstoque(12);
        produto.setQuantidadeMinima(4);
        produto.setDataCadastro(LocalDateTime.now());
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        produtoRepository.save(produto);

        List<Produto> produtos = produtoRepository.findBySku("SKU006");
        Assertions.assertFalse(produtos.isEmpty(), "Produtos nao encontrado.");

    }

    @Test
    public void testGetAllProdutosEstoqueBaixo(){
        Categoria categoria = new Categoria();
        categoria.setNome("Hardware");
        categoria.setDescricao("Pecas de hardware");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT950");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor hardware");
        fornecedor.setCnpj("12345678000100");
        fornecedor.setEmail("hardware@gmail.com");
        fornecedor.setTelefone("46888888888");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU950");
        produto.setNome("Memoria RAM");
        produto.setDescricao("Memoria RAM DDR4");
        produto.setMarca("Samsung");
        produto.setPreco(new BigDecimal("250.00"));
        produto.setQuantidadeEstoque(2);
        produto.setQuantidadeMinima(5);
        produto.setDataCadastro(LocalDateTime.now());
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        produtoRepository.save(produto);

        List<Produto> produtos = produtoRepository.getAllProdutosEstoqueBaixo();
        Assertions.assertFalse(produtos.isEmpty(), "nenhum produto com estoque baixo.");

    }

    @Test
    public void testGetAllByNomeLikeLimit(){
        Categoria categoria = new Categoria();
        categoria.setNome("perifericos");
        categoria.setDescricao("periferico");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT951");
        categoria.setAtivo(true);
        categoria = categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("fornecedor Perifericos");
        fornecedor.setCnpj("92000000000002");
        fornecedor.setEmail("perifiericos@gmail.com");
        fornecedor.setTelefone("46999992222");
        fornecedor.setAtivo(true);
        fornecedor = fornecedorRepository.save(fornecedor);

        List<Produto> produtos = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            Produto produto = new Produto();
            produto.setSku("SKU95" + i);
            produto.setNome("Mouse" + i);
            produto.setDescricao("Mouse teste" + i);
            produto.setMarca("Marca + i");
            produto.setPreco(new BigDecimal("100.00"));
            produto.setQuantidadeEstoque(20);
            produto.setQuantidadeMinima(5);
            produto.setDataCadastro(LocalDateTime.now());
            produto.setCategoria(categoria);
            produto.setFornecedor(fornecedor);
            produtos.add(produto);

        }

        produtoRepository.saveAll(produtos);

        List<Produto> produtosEncontrados = produtoRepository.getAllByNomeLikeLimit("Mouse", 10);
        Assertions.assertFalse(produtosEncontrados.isEmpty(), "nenhum produto encontrado");
        Assertions.assertEquals(10, produtosEncontrados.size(), "O numero de produtos encontrados nao corresponde ao limite");

    }

}
