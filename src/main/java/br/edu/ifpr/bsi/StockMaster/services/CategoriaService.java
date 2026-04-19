package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.CategoriaMapper;
import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Transactional
    public CategoriaDetailDTO salvar(CategoriaRequestDTO request) {
        Categoria categoria = this.categoriaMapper.requestDTOToEntity(request);
        return this.categoriaMapper.entityToDetailDTO(this.categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaSummaryDTO> listarTodos() {
        return this.categoriaRepository.findAll()
                .stream()
                .map(this.categoriaMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDetailDTO buscarPorId(Long id) {
        return this.categoriaMapper.entityToDetailDTO(buscarEntidade(id));
    }

    @Transactional
    public CategoriaDetailDTO atualizar(Long id, CategoriaRequestDTO request) {
        Categoria categoriaBanco = buscarEntidade(id);
        Categoria categoria = this.categoriaMapper.requestDTOToEntity(request);

        categoriaBanco.setNome(categoria.getNome());
        categoriaBanco.setDescricao(categoria.getDescricao());
        categoriaBanco.setSetor(categoria.getSetor());
        categoriaBanco.setCodigoInterno(categoria.getCodigoInterno());
        categoriaBanco.setAtivo(categoria.getAtivo());

        return this.categoriaMapper.entityToDetailDTO(this.categoriaRepository.save(categoriaBanco));
    }

    @Transactional
    public void deletar(Long id) {
        Categoria categoriaBanco = buscarEntidade(id);
        this.categoriaRepository.delete(categoriaBanco);
    }

    @Transactional(readOnly = true)
    public List<CategoriaSummaryDTO> buscarPorNome(String nome) {
        return this.categoriaRepository.findByNome(nome)
                .stream()
                .map(this.categoriaMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaSummaryDTO> buscarPorSetorLike(String setor) {
        return this.categoriaRepository.getAllBySetorLike(setor)
                .stream()
                .map(this.categoriaMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaSummaryDTO> buscarPorNomeLikeLimit(String nome, int limit) {
        return this.categoriaRepository.getAllByNomeLikeLimit(nome, limit)
                .stream()
                .map(this.categoriaMapper::entityToSummaryDTO)
                .toList();
    }

    private Categoria buscarEntidade(Long id) {
        return this.categoriaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria nao encontrada."));
    }
}
