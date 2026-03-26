package br.edu.ifpr.bsi.projetointegradorprog.ServiceTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.projetointegradorprog.services.CategoriaService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria criarCategoria(String nome, String setor, String codigoInterno){
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao("Descricao de " + nome);
        categoria.setSetor(setor);
        categoria.setCodigoInterno(codigoInterno);
        categoria.setAtivo(true);
        return categoriaRepository.save(categoria);
    }

    @Test
    public void testSalvar() {
        Categoria categoria = new Categoria();
        categoria.setNome("Informática");
        categoria.setDescricao("Produtos de informática");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT001");
        categoria.setAtivo(true);

        Categoria categoriaSalva = categoriaService.salvar(categoria);

        Assertions.assertNotNull(categoriaSalva);
        Assertions.assertNotNull(categoriaSalva.getId());
    }

    @Test
    public void testListarTodos() {
        criarCategoria("Escritório", "Administrativo", "CAT002");

        List<Categoria> categorias = categoriaService.listarTodos();

        Assertions.assertFalse(categorias.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Categoria categoria = criarCategoria("Limpeza", "Serviços", "CAT003");

        Categoria categoriaEncontrada = categoriaService.buscarPorId(categoria.getId());

        Assertions.assertNotNull(categoriaEncontrada);
        Assertions.assertEquals("Limpeza", categoriaEncontrada.getNome());
    }

    @Test
    public void testAtualizar() {
        Categoria categoria = criarCategoria("Ferramentas", "Manutenção", "CAT004");

        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome("Ferramentas Elétricas");
        novaCategoria.setDescricao("Ferramentas motorizadas");
        novaCategoria.setSetor("Manutenção");
        novaCategoria.setCodigoInterno("CAT004A");
        novaCategoria.setAtivo(true);

        Categoria categoriaAtualizada = categoriaService.atualizar(categoria.getId(), novaCategoria);

        Assertions.assertNotNull(categoriaAtualizada);
        Assertions.assertEquals("Ferramentas Elétricas", categoriaAtualizada.getNome());
        Assertions.assertEquals("CAT004A", categoriaAtualizada.getCodigoInterno());
    }

    @Test
    public void testDeletar() {
        Categoria categoria = criarCategoria("Hardware", "Tecnologia", "CAT005");

        boolean deletou = categoriaService.deletar(categoria.getId());
        Categoria categoriaEncontrada = categoriaRepository.findById(categoria.getId()).orElse(null);

        Assertions.assertTrue(deletou);
        Assertions.assertNull(categoriaEncontrada);
    }

    @Test
    public void testBuscarPorNome() {
        criarCategoria("Eletrônicos", "Tecnologia", "CAT006");

        List<Categoria> categorias = categoriaService.buscarPorNome("Eletrônicos");

        Assertions.assertFalse(categorias.isEmpty());
    }

    @Test
    public void testBuscarPorSetorLike() {
        criarCategoria("Monitores", "Tecnologia", "CAT007");

        List<Categoria> categorias = categoriaService.buscarPorSetorLike("Tec");

        Assertions.assertFalse(categorias.isEmpty());
    }

    @Test
    public void testBuscarPorNomeLikeLimit() {
        for (int i = 0; i < 12; i++) {
            criarCategoria("Categoria Teste " + i, "Setor Teste", "CAT90" + i);
        }

        List<Categoria> categorias = categoriaService.buscarPorNomeLikeLimit("Categoria", 10);

        Assertions.assertFalse(categorias.isEmpty());
        Assertions.assertEquals(10, categorias.size());
    }
}
