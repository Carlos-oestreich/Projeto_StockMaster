package br.edu.ifpr.bsi.StockMaster.RepositoryTest;

import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.repositories.FornecedorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class FornecedorRepositoryTest {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    public void testInsert(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Alfa");
        fornecedor.setCnpj("12345678000100");
        fornecedor.setEmail("alfa@gmail.com");
        fornecedor.setTelefone("46999999999");
        fornecedor.setAtivo(true);

        Fornecedor fornecedorInserido = fornecedorRepository.save(fornecedor);

        Fornecedor fornecedorEncontrado = fornecedorRepository.findById(fornecedor.getId()).orElse(null);
        Assertions.assertNotNull(fornecedorEncontrado, "O fornecedor nao foi inserido.");

    }

    @Test
    public void testUpdate(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Beta");
        fornecedor.setCnpj("22345678000100");
        fornecedor.setEmail("beta@gmail.com");
        fornecedor.setTelefone("46988888888");
        fornecedor.setAtivo(true);

        Fornecedor fornecedorAlterar = fornecedorRepository.save(fornecedor);
        fornecedorAlterar.setNome("Fornecedor Beta LTDA");

        Fornecedor fornecedorAlterado = fornecedorRepository.save(fornecedorAlterar);

        Fornecedor fornecedorEncontrado = fornecedorRepository.findById(fornecedorAlterado.getId()).orElse(null);
        Assertions.assertEquals("Fornecedor Beta LTDA", fornecedorEncontrado.getNome(), "O fornecedor nao foi atualizado.");

    }

    @Test
    public void testDelete(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Gama");
        fornecedor.setCnpj("32345678000100");
        fornecedor.setEmail("gama@gmail.com");
        fornecedor.setTelefone("46977777777");
        fornecedor.setAtivo(true);

        Fornecedor fornecedorDeletar = fornecedorRepository.save(fornecedor);
        fornecedorRepository.delete(fornecedorDeletar);

        Fornecedor fornecedorDeletado = fornecedorRepository.findById(fornecedor.getId()).orElse(null);
        Assertions.assertNull(fornecedorDeletado, "O fornecedor nao foi deletado.");

    }

    @Test
    public void testListar(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Delta");
        fornecedor.setCnpj("42345678000100");
        fornecedor.setEmail("delta@gmail.com");
        fornecedor.setTelefone("46966666666");
        fornecedor.setAtivo(true);

        fornecedorRepository.save(fornecedor);

        long inicio = System.currentTimeMillis();
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        long fim =  System.currentTimeMillis();

        Assertions.assertFalse(fornecedores.isEmpty(), "O fornecedor nao foi encontrado.");
        Assertions.assertTrue((fim - inicio) < 300, "A consulta demorou mais de 0,3 segundos.");

    }

    @Test
    public void testFindByNome(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Omega");
        fornecedor.setCnpj("52345678000100");
        fornecedor.setEmail("omega@gmail.com");
        fornecedor.setTelefone("46955555555");
        fornecedor.setAtivo(true);

        fornecedorRepository.save(fornecedor);

        List<Fornecedor> fornecedores = fornecedorRepository.findByNome("Fornecedor Omega");
        Assertions.assertFalse(fornecedores.isEmpty(), "O fornecedor nao foi encontrado.");

    }

    @Test
    public void testFindByCnpj(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Sigma");
        fornecedor.setCnpj("62345678000100");
        fornecedor.setEmail("sigma@gmail.com");
        fornecedor.setTelefone("46944444444");
        fornecedor.setAtivo(true);

        fornecedorRepository.save(fornecedor);

        List<Fornecedor> fornecedores = fornecedorRepository.findByCnpj("62345678000100");
        Assertions.assertFalse(fornecedores.isEmpty(), "O fornecedor nao foi encontrado.");

    }

    @Test
    public void testGetAllByNomeLike(){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Master");
        fornecedor.setCnpj("90345678000100");
        fornecedor.setEmail("master@gmail.com");
        fornecedor.setTelefone("46999990001");
        fornecedor.setAtivo(true);

        fornecedorRepository.save(fornecedor);

        List<Fornecedor> fornecedores = fornecedorRepository.getAllByNomeLike("Master");
        Assertions.assertFalse(fornecedores.isEmpty(), "O fornecedor nao encontrado.");
    }

    @Test
    public void testGetAllByAtivoLimit(){
        List<Fornecedor> fornecedores = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNome("Fornecedor Ativo" + i);
            fornecedor.setCnpj("90345678000100" + i);
            fornecedor.setEmail("ativo" + i + "@gmail.com");
            fornecedor.setTelefone("46955555555" + i);
            fornecedor.setAtivo(true);
            fornecedores.add(fornecedor);

        }

        fornecedorRepository.saveAll(fornecedores);

        List<Fornecedor> fornecedoresEncontrados = fornecedorRepository.getAllByAtivoLimit(true, 10);
        Assertions.assertFalse(fornecedoresEncontrados.isEmpty(), "O fornecedor nao encontrado.");
        Assertions.assertEquals(10, fornecedoresEncontrados.size(), "o numero de fornecedores encontrados nao corresponde ao limite estabelecido");
    }

}
