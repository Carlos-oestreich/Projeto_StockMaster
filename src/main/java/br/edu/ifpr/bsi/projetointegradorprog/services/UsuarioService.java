package br.edu.ifpr.bsi.projetointegradorprog.services;

import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario atualizar(Long id, Usuario usuario){
        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco == null){
            return null;
        }

        usuarioBanco.setNome(usuario.getNome());
        usuarioBanco.setEmail(usuario.getEmail());
        usuarioBanco.setSenha(usuario.getSenha());
        usuarioBanco.setPerfil(usuario.getPerfil());
        usuarioBanco.setMatricula(usuario.getMatricula());
        usuarioBanco.setAtivo(usuario.getAtivo());

        return usuarioRepository.save(usuarioBanco);
    }

    public boolean deletar(Long id){
        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco == null){
            return false;
        }

        usuarioRepository.delete(usuarioBanco);
        return true;
    }

    public List<Usuario> buscarPorNome(String nome){
        return usuarioRepository.findByNome(nome);
    }

    public List<Usuario> buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> buscarPorPerfil(String perfil){
        return usuarioRepository.getAllByPerfil(perfil);
    }

    public List<Usuario> buscarPorNomeLikeLimit(String nome, int limit){
        return usuarioRepository.getAllByNomeLikeLimit(nome, limit);
    }
}