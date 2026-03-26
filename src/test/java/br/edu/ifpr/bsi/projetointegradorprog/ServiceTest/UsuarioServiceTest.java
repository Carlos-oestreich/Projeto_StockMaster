package br.edu.ifpr.bsi.projetointegradorprog.ServiceTest;

import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.UsuarioRepository;
import br.edu.ifpr.bsi.projetointegradorprog.services.UsuarioService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuario(String nome, String email, String matricula) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha("123456");
        usuario.setPerfil("ADMIN");
        usuario.setMatricula(matricula);
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    @Test
    public void testSalvar() {
        Usuario usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("123456");
        usuario.setPerfil("ADMIN");
        usuario.setMatricula("MAT001");
        usuario.setAtivo(true);

        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertNotNull(usuarioSalvo.getId());
    }

    @Test
    public void testListarTodos() {
        criarUsuario("Ana", "ana@email.com", "MAT002");

        List<Usuario> usuarios = usuarioService.listarTodos();

        Assertions.assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Usuario usuario = criarUsuario("Marcos", "marcos@email.com", "MAT003");

        Usuario usuarioEncontrado = usuarioService.buscarPorId(usuario.getId());

        Assertions.assertNotNull(usuarioEncontrado);
        Assertions.assertEquals("Marcos", usuarioEncontrado.getNome());
    }

    @Test
    public void testAtualizar() {
        Usuario usuario = criarUsuario("Patricia", "patricia@email.com", "MAT004");

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Patricia Souza");
        novoUsuario.setEmail("patricia.souza@email.com");
        novoUsuario.setSenha("654321");
        novoUsuario.setPerfil("OPERADOR");
        novoUsuario.setMatricula("MAT004A");
        novoUsuario.setAtivo(true);

        Usuario usuarioAtualizado = usuarioService.atualizar(usuario.getId(), novoUsuario);

        Assertions.assertNotNull(usuarioAtualizado);
        Assertions.assertEquals("Patricia Souza", usuarioAtualizado.getNome());
        Assertions.assertEquals("OPERADOR", usuarioAtualizado.getPerfil());
    }

    @Test
    public void testDeletar() {
        Usuario usuario = criarUsuario("Juliana", "juliana@email.com", "MAT005");

        boolean deletou = usuarioService.deletar(usuario.getId());
        Usuario usuarioEncontrado = usuarioRepository.findById(usuario.getId()).orElse(null);

        Assertions.assertTrue(deletou);
        Assertions.assertNull(usuarioEncontrado);
    }

    @Test
    public void testBuscarPorNome() {
        criarUsuario("Roberto", "roberto@email.com", "MAT006");

        List<Usuario> usuarios = usuarioService.buscarPorNome("Roberto");

        Assertions.assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testBuscarPorEmail() {
        criarUsuario("Fernanda", "fernanda@email.com", "MAT007");

        List<Usuario> usuarios = usuarioService.buscarPorEmail("fernanda@email.com");

        Assertions.assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testBuscarPorPerfil() {
        criarUsuario("Lucas", "lucas@email.com", "MAT008");

        List<Usuario> usuarios = usuarioService.buscarPorPerfil("ADMIN");

        Assertions.assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testBuscarPorNomeLikeLimit() {
        for (int i = 0; i < 12; i++) {
            criarUsuario("Usuario Teste " + i, "usuario" + i + "@email.com", "MAT90" + i);
        }

        List<Usuario> usuarios = usuarioService.buscarPorNomeLikeLimit("Usuario", 10);

        Assertions.assertFalse(usuarios.isEmpty());
        Assertions.assertEquals(10, usuarios.size());
    }

}
