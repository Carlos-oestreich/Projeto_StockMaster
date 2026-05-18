package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.UsuarioMapper;
import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.EmpresaRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDetailDTO salvar(UsuarioRequestDTO request, Long empresaId) {
        Usuario usuario = this.usuarioMapper.requestDTOToEntity(request);
        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }
        usuario.setEmpresa(empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada.")));
        return this.usuarioMapper.entityToDetailDTO(this.usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> listarTodos(Long empresaId) {
        return this.usuarioRepository.findByEmpresaId(empresaId)
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
        usuarioBanco.setPerfil(usuario.getPerfil());
        usuarioBanco.setMatricula(usuario.getMatricula());
        usuarioBanco.setCpf(usuario.getCpf());
        usuarioBanco.setAtivo(usuario.getAtivo());

        if (request.senha() != null && !request.senha().isBlank()) {
            usuarioBanco.setSenha(passwordEncoder.encode(request.senha()));
        }

        return this.usuarioMapper.entityToDetailDTO(this.usuarioRepository.save(usuarioBanco));
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuarioBanco = buscarEntidade(id);
        this.usuarioRepository.delete(usuarioBanco);
    }



    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> buscarPorEmail(String email) {
        return this.usuarioRepository.findByEmail(email)
                .stream()
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> buscarPorNome(String nome) {
        return this.usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getNome().equalsIgnoreCase(nome))
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> buscarPorPerfil(String perfil) {
        return this.usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getPerfil().equalsIgnoreCase(perfil))
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioSummaryDTO> buscarPorNomeLikeLimit(String nome, int limit) {
        return this.usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getNome().toLowerCase().contains(nome.toLowerCase()))
                .limit(limit)
                .map(this.usuarioMapper::entityToSummaryDTO)
                .toList();
    }

    private Usuario buscarEntidade(Long id) {
        return this.usuarioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado."));
    }
}
