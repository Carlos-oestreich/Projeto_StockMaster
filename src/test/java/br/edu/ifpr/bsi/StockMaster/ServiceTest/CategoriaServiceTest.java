package br.edu.ifpr.bsi.StockMaster.ServiceTest;

import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.StockMaster.services.CategoriaService;
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

    private Categoria criarCategoria(String nome, String setor, String codigoInterno) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao("Descricao de " + nome);
        categoria.setSetor(setor);
        categoria.setCodigoInterno(codigoInterno);
        categoria.setAtivo(true);
        return categoriaRepository.save(categoria);
    }

    private CategoriaRequestDTO montarRequest(String nome, String setor, String codigoInterno) {
        return new CategoriaRequestDTO(
                nome,
                "Descricao de " + nome,
                setor,
                codigoInterno,
                true
        );
    }

    @Test
    public void testSalvar() {
        CategoriaDetailDTO categoriaSalva = categoriaService.salvar(
                montarRequest("Informatica", "Tecnologia", "CAT001")
        );

        Assertions.assertNotNull(categoriaSalva);
        Assertions.assertNotNull(categoriaSalva.id());
    }

    @Test
    public void testListarTodos() {
        criarCategoria("Escritorio", "Administrativo", "CAT002");

        List<CategoriaSummaryDTO> categorias = categoriaService.listarTodos();

        Assertions.assertFalse(categorias.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Categoria categoria = criarCategoria("Limpeza", "Servicos", "CAT003");

        CategoriaDetailDTO categoriaEncontrada = categoriaService.buscarPorId(categoria.getId());

        Assertions.assertNotNull(categoriaEncontrada);
        Assertions.assertEquals("Limpeza", categoriaEncontrada.nome());
    }

    @Test
    public void testAtualizar() {
        Categoria categoria = criarCategoria("Ferramentas", "Manutencao", "CAT004");

        CategoriaDetailDTO categoriaAtualizada = categoriaService.atualizar(
                categoria.getId(),
                new CategoriaRequestDTO(
                        "Ferramentas Eletricas",
                        "Ferramentas motorizadas",
                        "Manutencao",
                        "CAT004A",
                        true
                )
        );

        Assertions.assertNotNull(categoriaAtualizada);
        Assertions.assertEquals("Ferramentas Eletricas", categoriaAtualizada.nome());
        Assertions.assertEquals("CAT004A", categoriaAtualizada.codigoInterno());
    }

    @Test
    public void testDeletar() {
        Categoria categoria = criarCategoria("Hardware", "Tecnologia", "CAT005");

        categoriaService.deletar(categoria.getId());
        Categoria categoriaEncontrada = categoriaRepository.findById(categoria.getId()).orElse(null);

        Assertions.assertNull(categoriaEncontrada);
    }

    @Test
    public void testBuscarPorNome() {
        criarCategoria("Eletronicos", "Tecnologia", "CAT006");

        List<CategoriaSummaryDTO> categorias = categoriaService.buscarPorNome("Eletronicos");

        Assertions.assertFalse(categorias.isEmpty());
    }

    @Test
    public void testBuscarPorSetorLike() {
        criarCategoria("Monitores", "Tecnologia", "CAT007");

        List<CategoriaSummaryDTO> categorias = categoriaService.buscarPorSetorLike("Tec");

        Assertions.assertFalse(categorias.isEmpty());
    }

    @Test
    public void testBuscarPorNomeLikeLimit() {
        for (int i = 0; i < 12; i++) {
            criarCategoria("Categoria Teste " + i, "Setor Teste", "CAT90" + i);
        }

        List<CategoriaSummaryDTO> categorias = categoriaService.buscarPorNomeLikeLimit("Categoria", 10);

        Assertions.assertFalse(categorias.isEmpty());
        Assertions.assertEquals(10, categorias.size());
    }
}
