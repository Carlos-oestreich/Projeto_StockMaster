package br.edu.ifpr.bsi.projetointegradorprog.ServiceTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.FornecedorRepository;
import br.edu.ifpr.bsi.projetointegradorprog.services.FornecedorService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class FornecedorServiceTest {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private Fornecedor criarFornecedor(String nome, String cnpj, String email) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(nome);
        fornecedor.setCnpj(cnpj);
        fornecedor.setEmail(email);
        fornecedor.setTelefone("46999999999");
        fornecedor.setAtivo(true);
        return fornecedorRepository.save(fornecedor);
    }

    @Test
    public void testSalvar() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Alfa");
        fornecedor.setCnpj("12345678000100");
        fornecedor.setEmail("alfa@email.com");
        fornecedor.setTelefone("46999999999");
        fornecedor.setAtivo(true);

        Fornecedor fornecedorSalvo = fornecedorService.salvar(fornecedor);

        Assertions.assertNotNull(fornecedorSalvo);
        Assertions.assertNotNull(fornecedorSalvo.getId());
    }

    @Test
    public void testListarTodos() {
        criarFornecedor("Fornecedor Beta", "22345678000100", "beta@email.com");

        List<Fornecedor> fornecedores = fornecedorService.listarTodos();

        Assertions.assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Fornecedor fornecedor = criarFornecedor("Fornecedor Gama", "32345678000100", "gama@email.com");

        Fornecedor fornecedorEncontrado = fornecedorService.buscarPorId(fornecedor.getId());

        Assertions.assertNotNull(fornecedorEncontrado);
        Assertions.assertEquals("Fornecedor Gama", fornecedorEncontrado.getNome());
    }

    @Test
    public void testAtualizar() {
        Fornecedor fornecedor = criarFornecedor("Fornecedor Delta", "42345678000100", "delta@email.com");

        Fornecedor novoFornecedor = new Fornecedor();
        novoFornecedor.setNome("Fornecedor Delta LTDA");
        novoFornecedor.setCnpj("42345678000100");
        novoFornecedor.setEmail("delta.ltda@email.com");
        novoFornecedor.setTelefone("46888888888");
        novoFornecedor.setAtivo(true);

        Fornecedor fornecedorAtualizado = fornecedorService.atualizar(fornecedor.getId(), novoFornecedor);

        Assertions.assertNotNull(fornecedorAtualizado);
        Assertions.assertEquals("Fornecedor Delta LTDA", fornecedorAtualizado.getNome());
        Assertions.assertEquals("delta.ltda@email.com", fornecedorAtualizado.getEmail());
    }

    @Test
    public void testDeletar() {
        Fornecedor fornecedor = criarFornecedor("Fornecedor Ômega", "52345678000100", "omega@email.com");

        boolean deletou = fornecedorService.deletar(fornecedor.getId());
        Fornecedor fornecedorEncontrado = fornecedorRepository.findById(fornecedor.getId()).orElse(null);

        Assertions.assertTrue(deletou);
        Assertions.assertNull(fornecedorEncontrado);
    }

    @Test
    public void testBuscarPorNome() {
        criarFornecedor("Fornecedor Sigma", "62345678000100", "sigma@email.com");

        List<Fornecedor> fornecedores = fornecedorService.buscarPorNome("Fornecedor Sigma");

        Assertions.assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testBuscarPorCnpj() {
        criarFornecedor("Fornecedor Master", "72345678000100", "master@email.com");

        List<Fornecedor> fornecedores = fornecedorService.buscarPorCnpj("72345678000100");

        Assertions.assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testBuscarPorNomeLike() {
        criarFornecedor("Fornecedor Especial", "82345678000100", "especial@email.com");

        List<Fornecedor> fornecedores = fornecedorService.buscarPorNomeLike("Especial");

        Assertions.assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testBuscarPorAtivoLimit() {
        for (int i = 0; i < 12; i++) {
            criarFornecedor("Fornecedor Ativo " + i, "900000000000" + i, "ativo" + i + "@email.com");
        }

        List<Fornecedor> fornecedores = fornecedorService.buscarPorAtivoLimit(true, 10);

        Assertions.assertFalse(fornecedores.isEmpty());
        Assertions.assertEquals(10, fornecedores.size());
    }

}
