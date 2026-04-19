package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.UsuarioMapper;
import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioDetailDTO salvar(UsuarioRequestDTO request) {
        Usuario usuario = this.usuarioMapper.requestDTOToEntity(request);
        return this.usuarioMapper.entityToDetailDTO(this.usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> listarTodos() {
        return this.usuarioRepository.findAll()
                .stream()
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDetailDTO buscarPorId(Long id) {
        return this.usuarioMapper.entityToDetailDTO(buscarEntidade(id));
    }

    @Transactional
    public UsuarioDetailDTO atualizar(Long id, UsuarioRequestDTO request) {
        Usuario usuarioBanco = buscarEntidade(id);
        Usuario usuario = this.usuarioMapper.requestDTOToEntity(request);

        usuarioBanco.setNome(usuario.getNome());
        usuarioBanco.setEmail(usuario.getEmail());
        usuarioBanco.setSenha(usuario.getSenha());
        usuarioBanco.setPerfil(usuario.getPerfil());
        usuarioBanco.setMatricula(usuario.getMatricula());
        usuarioBanco.setAtivo(usuario.getAtivo());

        return this.usuarioMapper.entityToDetailDTO(this.usuarioRepository.save(usuarioBanco));
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuarioBanco = buscarEntidade(id);
        this.usuarioRepository.delete(usuarioBanco);
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> buscarPorNome(String nome) {
        return this.usuarioRepository.findByNome(nome)
                .stream()
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> buscarPorEmail(String email) {
        return this.usuarioRepository.findByEmail(email)
                .stream()
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> buscarPorPerfil(String perfil) {
        return this.usuarioRepository.getAllByPerfil(perfil)
                .stream()
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> buscarPorNomeLikeLimit(String nome, int limit) {
        return this.usuarioRepository.getAllByNomeLikeLimit(nome, limit)
                .stream()
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    private Usuario buscarEntidade(Long id) {
        return this.usuarioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado."));
    }
}
