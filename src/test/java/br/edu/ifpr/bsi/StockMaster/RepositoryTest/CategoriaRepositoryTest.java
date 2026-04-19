package br.edu.ifpr.bsi.StockMaster.RepositoryTest;

import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.repositories.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    public void testInsert(){
        Categoria categoria = new Categoria();
        categoria.setNome("informatica");
        categoria.setDescricao("Produtos de informatica");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT001");
        categoria.setAtivo(true);

        Categoria categoriaInserida = categoriaRepository.save(categoria);

        Categoria categoriaEncontrada = categoriaRepository.findById(categoriaInserida.getId()).orElse(null);
        Assertions.assertNotNull(categoriaEncontrada, "A categoria nao foi inserida.");
    }

    @Test
    public void testUpdate(){
        Categoria categoria = new Categoria();
        categoria.setNome("Limpeza");
        categoria.setDescricao("Produtos de Limpeza");
        categoria.setSetor("Servicos");
        categoria.setCodigoInterno("CAT002");
        categoria.setAtivo(true);

        Categoria categoriaAlterar = categoriaRepository.save(categoria);
        categoriaAlterar.setNome("Limpeza Geral");

        Categoria categoriaAlterada = categoriaRepository.save(categoriaAlterar);

        Categoria categoriaEncontrada = categoriaRepository.findById(categoriaAlterada.getId()).orElse(null);
        Assertions.assertEquals("Limpeza Geral", categoriaEncontrada.getNome(), "o nome da categoria nao foi atualizado.");

    }

    @Test
    public void testDelete(){
        Categoria categoria = new Categoria();
        categoria.setNome("Escritorio");
        categoria.setDescricao("Materiais de Escritorio");
        categoria.setSetor("Administrativo");
        categoria.setCodigoInterno("CAT003");
        categoria.setAtivo(true);

        Categoria categoriaDeletar = categoriaRepository.save(categoria);
        categoriaRepository.delete(categoriaDeletar);

        Categoria categoriaDeletada = categoriaRepository.findById(categoriaDeletar.getId()).orElse(null);
        Assertions.assertNull(categoriaDeletada, "A categoria nao foi deletada.");

    }

    @Test
    public void testListar(){
        Categoria categoria = new Categoria();
        categoria.setNome("Ferramentas");
        categoria.setDescricao("Ferramentas em geral");
        categoria.setSetor("manutencao");
        categoria.setCodigoInterno("CAT004");
        categoria.setAtivo(true);

        categoriaRepository.save(categoria);

        long inicio = System.currentTimeMillis();
        List<Categoria> categorias = categoriaRepository.findAll();
        long fim = System.currentTimeMillis();

        Assertions.assertFalse(categorias.isEmpty(), "Categoria nao encontrada.");
        Assertions.assertTrue((fim - inicio) < 300, "A consulta demorou mais de 0,3 segundos.");

    }

    @Test
    public void testFindByNome(){
        Categoria categoria = new Categoria();
        categoria.setNome("Eletronicos");
        categoria.setDescricao("Equipamentos eletronicos");
        categoria.setSetor("Tecnologia");
        categoria.setCodigoInterno("CAT005");
        categoria.setAtivo(true);

        categoriaRepository.save(categoria);

        List<Categoria> categorias = categoriaRepository.findByNome("Eletronicos");
        Assertions.assertFalse(categorias.isEmpty(), "Categoria nao encontrada.");

    }

    @Test
    public void testFindBySetorLike(){
        Categoria categoria = new Categoria();
        categoria.setNome("Informatica");
        categoria.setDescricao("Informatica em geral");
        categoria.setSetor("Administrativo");
        categoria.setCodigoInterno("CAT900");
        categoria.setAtivo(true);
        categoriaRepository.save(categoria);

        List<Categoria> categorias = categoriaRepository.getAllBySetorLike("Adm");
        Assertions.assertFalse(categorias.isEmpty(), "Categoria nao encontrada.");

    }

    @Test
    public void testGetAllByNomeLikeLimit(){
        List<Categoria> categorias = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Categoria categoria = new Categoria();
            categoria.setNome("Categoria " + i);
            categoria.setDescricao("Descricao " + i);
            categoria.setSetor("setor teste");
            categoria.setCodigoInterno("CAT90" + i);
            categoria.setAtivo(true);
            categorias.add(categoria);
        }

        categoriaRepository.saveAll(categorias);

        List<Categoria> categoriasEncontradas = categoriaRepository.getAllByNomeLikeLimit("Categoria", 10);
        Assertions.assertFalse( categoriasEncontradas.isEmpty(), "Categoria nao encontrada.");
        Assertions.assertEquals(10, categoriasEncontradas.size(), "O numero de categorias nao encontradas corresponde ao limite.");

    }


}
