package br.edu.ifpr.bsi.projetointegradorprog.RepositoryTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.MovimentacaoEstoque;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class MovimentacaoEstoqueRepositoryTest {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private Produto criarProduto(){
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria teste");
        categoria.setDescricao("descricao teste");
        categoria.setSetor("setor teste");
        categoria.setCodigoInterno("CAT200");
        categoria.setAtivo(true);
        categoria =  categoriaRepository.save(categoria);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("fornecedor teste");
        fornecedor.setCnpj("13345678000100");
        fornecedor.setEmail("fornecedor@gmail.com");
        fornecedor.setTelefone("46777777777");
        fornecedor.setAtivo(true);
        fornecedor =  fornecedorRepository.save(fornecedor);

        Produto produto = new Produto();
        produto.setSku("SKU100");
        produto.setNome("Produto teste");
        produto.setDescricao("Descricao produto");
        produto.setMarca("Marca x");
        produto.setQuantidadeEstoque(50);
        produto.setQuantidadeMinima(10);
        produto.setDataCadastro(LocalDateTime.now());
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        return produtoRepository.save(produto);

    }

    private Usuario criarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario teste");
        usuario.setEmail("usuario@gmail.com");
        usuario.setSenha("123456");
        usuario.setPerfil("ADMIN");
        usuario.setMatricula("MAT100");
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);

    }

    @Test
    public void testInsert(){
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo("ENTRADA");
        movimentacao.setQuantidade(10);
        movimentacao.setObservacao("Entrada inicial");
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setSaldoAnterior(50);
        movimentacao.setSaldoAtual(60);
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);

        MovimentacaoEstoque movimentacaoInserida = movimentacaoEstoqueRepository.save(movimentacao);

        MovimentacaoEstoque movimentacaoEncontrada = movimentacaoEstoqueRepository.findById(movimentacaoInserida.getId()).orElse(null);
        Assertions.assertNotNull(movimentacaoEncontrada, "A movimentcao nao foi finalizada");

    }

    @Test
    public void testUpdate(){
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo("SAIDA");
        movimentacao.setQuantidade(5);
        movimentacao.setObservacao("Saida teste");
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setSaldoAnterior(50);
        movimentacao.setSaldoAtual(45);
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);

        MovimentacaoEstoque movimentacaoAlterar = movimentacaoEstoqueRepository.save(movimentacao);
        movimentacaoAlterar.setObservacao("saida alterada");

        MovimentacaoEstoque movimentacaoEncontrada = movimentacaoEstoqueRepository.findById(movimentacaoAlterar.getId()).orElse(null);
        Assertions.assertEquals("saida alterada", movimentacaoEncontrada.getObservacao(), "A movimentacao nao foi alterada.");

    }

    @Test
    public void testDelete(){
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo("ENTRADA");
        movimentacao.setQuantidade(20);
        movimentacao.setObservacao("Entrada para delete");
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setSaldoAnterior(50);
        movimentacao.setSaldoAtual(70);
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);

        MovimentacaoEstoque movimentacaoDeletar = movimentacaoEstoqueRepository.save(movimentacao);
        movimentacaoEstoqueRepository.delete(movimentacaoDeletar);

        MovimentacaoEstoque movimentacaoDeletada = movimentacaoEstoqueRepository.findById(movimentacaoDeletar.getId()).orElse(null);
        Assertions.assertNull(movimentacaoDeletada, "A movimentacao nao foi deletada.");

    }

    @Test
    public void testListar(){
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo("Entrada");
        movimentacao.setQuantidade(15);
        movimentacao.setObservacao("Entrada para listagem");
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setSaldoAnterior(50);
        movimentacao.setSaldoAtual(65);
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);

        movimentacaoEstoqueRepository.save(movimentacao);

        long inicio = System.currentTimeMillis();
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findAll();
        long fim = System.currentTimeMillis();

        Assertions.assertFalse(movimentacoes.isEmpty(), "A movimentacoes nao encontrada.");
        Assertions.assertTrue((fim - inicio) < 300, "A consulta demorou mais de 0,3 segundos.");

    }

    @Test
    public void testFindByTipo(){
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo("SAIDA");
        movimentacao.setQuantidade(8);
        movimentacao.setObservacao("saida por venda");
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setSaldoAnterior(50);
        movimentacao.setSaldoAtual(42);
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);

        movimentacaoEstoqueRepository.save(movimentacao);

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findByTipo("SAIDA");
        Assertions.assertFalse(movimentacoes.isEmpty(), "A movimentacoes nao encontrada.");

    }

    @Test
    public void testGetAllByQuantidadeMaiorIgual(){
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setTipo("ENTRADA");
        movimentacao.setQuantidade(15);
        movimentacao.setObservacao("ENTRADA de teste");
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        movimentacao.setSaldoAnterior(10);
        movimentacao.setSaldoAtual(25);
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);

        movimentacaoEstoqueRepository.save(movimentacao);

        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.getAllByQuantidadeMaiorIgual(10);
        Assertions.assertFalse(movimentacoes.isEmpty(), "nenhuma movimentacoes encontrada com quantidade maior ou igual ao valor informado.");

    }

    @Test
    public void testGetAllByTipoLimit(){
        Produto produto = criarProduto();
        Usuario usuario = criarUsuario();

        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setTipo("SAIDA");
            movimentacao.setQuantidade(2 + i);
            movimentacao.setObservacao("saida " + i);
            movimentacao.setDataMovimentacao(LocalDateTime.now());
            movimentacao.setSaldoAnterior(50);
            movimentacao.setSaldoAtual(48 - i);
            movimentacao.setProduto(produto);
            movimentacao.setUsuario(usuario);
            movimentacoes.add(movimentacao);

        }

        movimentacaoEstoqueRepository.saveAll(movimentacoes);

        List<MovimentacaoEstoque> movimentacoesEncontradas = movimentacaoEstoqueRepository.getAllByTipoLimit("SAIDA", 10);
        Assertions.assertFalse(movimentacoesEncontradas.isEmpty(), "nenhuma movimentacao encontrada.");
        Assertions.assertEquals(10, movimentacoesEncontradas.size(), "o numero de movimentacoes encontradas nao corresponde ao limite definido.");

    }
}
