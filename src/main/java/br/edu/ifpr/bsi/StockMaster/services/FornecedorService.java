package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.FornecedorMapper;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private FornecedorMapper fornecedorMapper;

    @Transactional
    public FornecedorDetailDTO salvar(FornecedorRequestDTO request) {
        Fornecedor fornecedor = this.fornecedorMapper.requestDTOToEntity(request);
        return this.fornecedorMapper.entityToDetailDTO(this.fornecedorRepository.save(fornecedor));
    }

    @Transactional(readOnly = true)
    public List<FornecedorSummaryDTO> listarTodos() {
        return this.fornecedorRepository.findAll()
                .stream()
                .map(this.fornecedorMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public FornecedorDetailDTO buscarPorId(Long id) {
        return this.fornecedorMapper.entityToDetailDTO(buscarEntidade(id));
    }

    @Transactional
    public FornecedorDetailDTO atualizar(Long id, FornecedorRequestDTO request) {
        Fornecedor fornecedorBanco = buscarEntidade(id);
        Fornecedor fornecedor = this.fornecedorMapper.requestDTOToEntity(request);

        fornecedorBanco.setNome(fornecedor.getNome());
        fornecedorBanco.setCnpj(fornecedor.getCnpj());
        fornecedorBanco.setEmail(fornecedor.getEmail());
        fornecedorBanco.setTelefone(fornecedor.getTelefone());
        fornecedorBanco.setAtivo(fornecedor.getAtivo());

        return this.fornecedorMapper.entityToDetailDTO(this.fornecedorRepository.save(fornecedorBanco));
    }

    @Transactional
    public void deletar(Long id) {
        Fornecedor fornecedorBanco = buscarEntidade(id);
        this.fornecedorRepository.delete(fornecedorBanco);
    }

    @Transactional(readOnly = true)
    public List<FornecedorSummaryDTO> buscarPorNome(String nome) {
        return this.fornecedorRepository.findByNome(nome)
                .stream()
                .map(this.fornecedorMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FornecedorSummaryDTO> buscarPorCnpj(String cnpj) {
        return this.fornecedorRepository.findByCnpj(cnpj)
                .stream()
                .map(this.fornecedorMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FornecedorSummaryDTO> buscarPorNomeLike(String nome) {
        return this.fornecedorRepository.getAllByNomeLike(nome)
                .stream()
                .map(this.fornecedorMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FornecedorSummaryDTO> buscarPorAtivoLimit(boolean ativo, int limit) {
        return this.fornecedorRepository.getAllByAtivoLimit(ativo, limit)
                .stream()
                .map(this.fornecedorMapper::entityToSummaryDTO)
                .toList();
    }

    private Fornecedor buscarEntidade(Long id) {
        return this.fornecedorRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor nao encontrado."));
    }
}
