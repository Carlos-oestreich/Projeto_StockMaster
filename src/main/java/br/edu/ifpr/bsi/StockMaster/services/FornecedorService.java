package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.FornecedorMapper;
import br.edu.ifpr.bsi.StockMaster.model.empresa.Empresa;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.EmpresaRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class FornecedorService {

    @Autowired private FornecedorRepository fornecedorRepository;
    @Autowired private FornecedorMapper fornecedorMapper;
    @Autowired private EmpresaRepository empresaRepository;

    private Empresa buscarEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada."));
    }

    @Transactional
    public FornecedorDetailDTO salvar(FornecedorRequestDTO request, Long empresaId) {
        Fornecedor fornecedor = fornecedorMapper.requestDTOToEntity(request);
        fornecedor.setEmpresa(buscarEmpresa(empresaId));
        return fornecedorMapper.entityToDetailDTO(fornecedorRepository.save(fornecedor));
    }

    @Transactional(readOnly = true)
    public List<FornecedorSummaryDTO> listarTodos(Long empresaId) {
        return fornecedorRepository.findByEmpresaId(empresaId)
                .stream().map(fornecedorMapper::entityToSummaryDTO).toList();
    }

    @Transactional(readOnly = true)
    public FornecedorDetailDTO buscarPorId(Long id) {
        return fornecedorMapper.entityToDetailDTO(buscarEntidade(id));
    }

    @Transactional
    public FornecedorDetailDTO atualizar(Long id, FornecedorRequestDTO request) {
        Fornecedor fornecedorBanco = buscarEntidade(id);
        fornecedorBanco.setNome(request.nome());
        fornecedorBanco.setCnpj(request.cnpj());
        fornecedorBanco.setEmail(request.email());
        fornecedorBanco.setTelefone(request.telefone());
        fornecedorBanco.setAtivo(request.ativo());
        return fornecedorMapper.entityToDetailDTO(fornecedorRepository.save(fornecedorBanco));
    }

    @Transactional
    public void deletar(Long id) {
        fornecedorRepository.delete(buscarEntidade(id));
    }

    private Fornecedor buscarEntidade(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor nao encontrado."));
    }
}