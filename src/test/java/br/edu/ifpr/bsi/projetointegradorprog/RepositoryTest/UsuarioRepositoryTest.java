package br.edu.ifpr.bsi.projetointegradorprog.RepositoryTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testInsert(){
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@gmail.com");
        usuario.setSenha("123456");
        usuario.setPerfil("ADMIN");
        usuario.setMatricula("MAT001");
        usuario.setAtivo(true);

        Usuario usuarioInserido = usuarioRepository.save(usuario);

        Usuario usuarioEcontrado = usuarioRepository.findById(usuarioInserido.getId()).orElse(null);
        Assertions.assertNotNull(usuarioEcontrado, "O usuario nao foi inserido.");

    }

    @Test
    public void testUpdate(){
        Usuario usuario = new Usuario();
        usuario.setNome("Larissa");
        usuario.setEmail("larissa@gmail.com");
        usuario.setSenha("123456");
        usuario.setPerfil("OPERADOR");
        usuario.setMatricula("MAT002");
        usuario.setAtivo(true);

        Usuario usuarioAlterar = usuarioRepository.save(usuario);
        usuarioAlterar.setNome("Larissa Laumann");

        Usuario usuarioAlterado = usuarioRepository.save(usuarioAlterar);

        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioAlterado.getId()).orElse(null);
        Assertions.assertEquals("Larissa Laumann", usuarioEncontrado.getNome(), "O usuario nao foi atualizado");

    }

    @Test
    public void testDelete(){
        Usuario usuario = new Usuario();
        usuario.setNome("Joao");
        usuario.setEmail("joao@gmail.com");
        usuario.setSenha("123456");
        usuario.setPerfil("OPERADOR");
        usuario.setMatricula("MAT003");
        usuario.setAtivo(true);

        Usuario usuarioDeletar = usuarioRepository.save(usuario);
        usuarioRepository.delete(usuarioDeletar);

        Usuario usuarioDeletado = usuarioRepository.findById(usuarioDeletar.getId()).orElse(null);
        Assertions.assertNull(usuarioDeletado, "O usuario nao foi deletado");

    }

    @Test
    public void testListar(){
        Usuario usuario = new Usuario();
        usuario.setNome("Douglas");
        usuario.setEmail("douglas@gmail.com");
        usuario.setSenha("123456");
        usuario.setPerfil("OPERADOR");
        usuario.setMatricula("MAT004");
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        long inicio = System.currentTimeMillis();
        List<Usuario> usuarios = usuarioRepository.findAll();
        long fim = System.currentTimeMillis();

        Assertions.assertFalse(usuarios.isEmpty(), "O usuario nao foi encontrado.");
        Assertions.assertTrue((fim - inicio) < 300, "A consulta demorou mais de 0,3 segundos.");

    }

    @Test
    public void testFindByNome(){
        Usuario usuario = new Usuario();
        usuario.setNome("Mateus");
        usuario.setEmail("mateus@gmail.com");
        usuario.setSenha("123456");
        usuario.setPerfil("OPERADOR");
        usuario.setMatricula("MAT005");
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);
        List<Usuario> usuarios = usuarioRepository.findByNome("Mateus");
        Assertions.assertFalse(usuarios.isEmpty(), "O usuario nao foi encontrado.");

    }

    @Test
    public void testFindByEmail(){
        Usuario usuario = new Usuario();
        usuario.setNome("Roberto");
        usuario.setEmail("roberto@gmail.com");
        usuario.setSenha("123456");
        usuario.setPerfil("OPERADOR");
        usuario.setMatricula("M0T006");
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        List<Usuario> usuarios = usuarioRepository.findByEmail("roberto@gmail.com");
        Assertions.assertFalse(usuarios.isEmpty(), "O usuario nao foi encontrado.");

    }

    @Test
    public void testGetAllByPerfil(){
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos.perfil@gmail.com");
        usuario.setSenha("123456");
        usuario.setPerfil("ADMIN");

        usuarioRepository.save(usuario);

        List<Usuario> usuarios = usuarioRepository.getAllByPerfil("ADMIN");
        Assertions.assertFalse(usuarios.isEmpty(), "O usuario nao foi encontrado.");

    }

    @Test
    public void testGetAllByNomeLikeLimit(){
        List<Usuario> usuarios = new ArrayList<>();

        for(int i = 0; i < 12; i++){
            Usuario usuario = new Usuario();
            usuario.setNome("Usuario Teste" + i);
            usuario.setEmail("usuario" + i + "@gmail.com");
            usuario.setSenha("123456");
            usuario.setPerfil("OPERADOR");
            usuario.setMatricula("MAT91" + i);
            usuario.setAtivo(true);
            usuarios.add(usuario);

        }

        usuarioRepository.saveAll(usuarios);

        List<Usuario> usuariosEncontrados = usuarioRepository.getAllByNomeLikeLimit("Usuario", 10);
        Assertions.assertFalse(usuariosEncontrados.isEmpty(), "O usuario nao encontrado.");
        Assertions.assertEquals(10, usuariosEncontrados.size(), "O numero de usuarios nao corresponde ao limite.");

    }

}
