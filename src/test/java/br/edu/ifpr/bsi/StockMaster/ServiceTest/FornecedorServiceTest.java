package br.edu.ifpr.bsi.StockMaster.ServiceTest;

import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.FornecedorRepository;
import br.edu.ifpr.bsi.StockMaster.services.FornecedorService;
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

    private FornecedorRequestDTO montarRequest(String nome, String cnpj, String email) {
        return new FornecedorRequestDTO(
                nome,
                cnpj,
                email,
                "46999999999",
                true
        );
    }

    @Test
    public void testSalvar() {
        FornecedorDetailDTO fornecedorSalvo = fornecedorService.salvar(
                montarRequest("Fornecedor Alfa", "12345678000100", "alfa@email.com")
        );

        Assertions.assertNotNull(fornecedorSalvo);
        Assertions.assertNotNull(fornecedorSalvo.id());
    }

    @Test
    public void testListarTodos() {
        criarFornecedor("Fornecedor Beta", "22345678000100", "beta@email.com");

        List<FornecedorSummaryDTO> fornecedores = fornecedorService.listarTodos();

        Assertions.assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Fornecedor fornecedor = criarFornecedor("Fornecedor Gama", "32345678000100", "gama@email.com");

        FornecedorDetailDTO fornecedorEncontrado = fornecedorService.buscarPorId(fornecedor.getId());

        Assertions.assertNotNull(fornecedorEncontrado);
        Assertions.assertEquals("Fornecedor Gama", fornecedorEncontrado.nome());
    }

    @Test
    public void testAtualizar() {
        Fornecedor fornecedor = criarFornecedor("Fornecedor Delta", "42345678000100", "delta@email.com");

        FornecedorDetailDTO fornecedorAtualizado = fornecedorService.atualizar(
                fornecedor.getId(),
                new FornecedorRequestDTO(
                        "Fornecedor Delta LTDA",
                        "42345678000100",
                        "delta.ltda@email.com",
                        "46888888888",
                        true
                )
        );

        Assertions.assertNotNull(fornecedorAtualizado);
        Assertions.assertEquals("Fornecedor Delta LTDA", fornecedorAtualizado.nome());
        Assertions.assertEquals("delta.ltda@email.com", fornecedorAtualizado.email());
    }

    @Test
    public void testDeletar() {
        Fornecedor fornecedor = criarFornecedor("Fornecedor Omega", "52345678000100", "omega@email.com");

        fornecedorService.deletar(fornecedor.getId());
        Fornecedor fornecedorEncontrado = fornecedorRepository.findById(fornecedor.getId()).orElse(null);

        Assertions.assertNull(fornecedorEncontrado);
    }

    @Test
    public void testBuscarPorNome() {
        criarFornecedor("Fornecedor Sigma", "62345678000100", "sigma@email.com");

        List<FornecedorSummaryDTO> fornecedores = fornecedorService.buscarPorNome("Fornecedor Sigma");

        Assertions.assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testBuscarPorCnpj() {
        criarFornecedor("Fornecedor Master", "72345678000100", "master@email.com");

        List<FornecedorSummaryDTO> fornecedores = fornecedorService.buscarPorCnpj("72345678000100");

        Assertions.assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testBuscarPorNomeLike() {
        criarFornecedor("Fornecedor Especial", "82345678000100", "especial@email.com");

        List<FornecedorSummaryDTO> fornecedores = fornecedorService.buscarPorNomeLike("Especial");

        Assertions.assertFalse(fornecedores.isEmpty());
    }

    @Test
    public void testBuscarPorAtivoLimit() {
        for (int i = 0; i < 12; i++) {
            criarFornecedor("Fornecedor Ativo " + i, "900000000000" + i, "ativo" + i + "@email.com");
        }

        List<FornecedorSummaryDTO> fornecedores = fornecedorService.buscarPorAtivoLimit(true, 10);

        Assertions.assertFalse(fornecedores.isEmpty());
        Assertions.assertEquals(10, fornecedores.size());
    }
}
