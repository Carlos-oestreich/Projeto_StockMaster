package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.CategoriaMapper;
import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.empresa.Empresa;
import br.edu.ifpr.bsi.StockMaster.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private CategoriaMapper categoriaMapper;
    @Autowired private EmpresaRepository empresaRepository;

    private Empresa buscarEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada."));
    }

    @Transactional
    public CategoriaDetailDTO salvar(CategoriaRequestDTO request, Long empresaId) {
        Categoria categoria = categoriaMapper.requestDTOToEntity(request);
        categoria.setEmpresa(buscarEmpresa(empresaId));
        return categoriaMapper.entityToDetailDTO(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaSummaryDTO> listarTodos(Long empresaId) {
        return categoriaRepository.findByEmpresaId(empresaId)
                .stream().map(categoriaMapper::entityToSummaryDTO).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDetailDTO buscarPorId(Long id) {
        return categoriaMapper.entityToDetailDTO(buscarEntidade(id));
    }

    @Transactional
    public CategoriaDetailDTO atualizar(Long id, CategoriaRequestDTO request) {
        Categoria categoriaBanco = buscarEntidade(id);
        categoriaBanco.setNome(request.nome());
        categoriaBanco.setDescricao(request.descricao());
        categoriaBanco.setSetor(request.setor());
        categoriaBanco.setCodigoInterno(request.codigoInterno());
        categoriaBanco.setAtivo(request.ativo());
        return categoriaMapper.entityToDetailDTO(categoriaRepository.save(categoriaBanco));
    }

    @Transactional
    public void deletar(Long id) {
        categoriaRepository.delete(buscarEntidade(id));
    }

    private Categoria buscarEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria nao encontrada."));
    }
}