package br.edu.ifpr.bsi.StockMaster.ServiceTest;

import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.UsuarioRepository;
import br.edu.ifpr.bsi.StockMaster.services.UsuarioService;
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

    private UsuarioRequestDTO montarRequest(String nome, String email, String matricula) {
        return new UsuarioRequestDTO(
                nome,
                email,
                "123456",
                "ADMIN",
                matricula,
                true
        );
    }

    @Test
    public void testSalvar() {
        UsuarioDetailDTO usuarioSalvo = usuarioService.salvar(
                montarRequest("Carlos", "carlos@email.com", "MAT001")
        );

        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertNotNull(usuarioSalvo.id());
    }

    @Test
    public void testListarTodos() {
        criarUsuario("Ana", "ana@email.com", "MAT002");

        List<UsuarioSummaryDTO> usuarios = usuarioService.listarTodos();

        Assertions.assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Usuario usuario = criarUsuario("Marcos", "marcos@email.com", "MAT003");

        UsuarioDetailDTO usuarioEncontrado = usuarioService.buscarPorId(usuario.getId());

        Assertions.assertNotNull(usuarioEncontrado);
        Assertions.assertEquals("Marcos", usuarioEncontrado.nome());
    }

    @Test
    public void testAtualizar() {
        Usuario usuario = criarUsuario("Patricia", "patricia@email.com", "MAT004");

        UsuarioDetailDTO usuarioAtualizado = usuarioService.atualizar(
                usuario.getId(),
                new UsuarioRequestDTO(
                        "Patricia Souza",
                        "patricia.souza@email.com",
                        "654321",
                        "OPERADOR",
                        "MAT004A",
                        true
                )
        );

        Assertions.assertNotNull(usuarioAtualizado);
        Assertions.assertEquals("Patricia Souza", usuarioAtualizado.nome());
        Assertions.assertEquals("OPERADOR", usuarioAtualizado.perfil());
    }

    @Test
    public void testDeletar() {
        Usuario usuario = criarUsuario("Juliana", "juliana@email.com", "MAT005");

        usuarioService.deletar(usuario.getId());
        Usuario usuarioEncontrado = usuarioRepository.findById(usuario.getId()).orElse(null);

        Assertions.assertNull(usuarioEncontrado);
    }

    @Test
    public void testBuscarPorNome() {
        criarUsuario("Roberto", "roberto@email.com", "MAT006");

        List<UsuarioSummaryDTO> usuarios = usuarioService.buscarPorNome("Roberto");

        Assertions.assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testBuscarPorEmail() {
        criarUsuario("Fernanda", "fernanda@email.com", "MAT007");

        List<UsuarioSummaryDTO> usuarios = usuarioService.buscarPorEmail("fernanda@email.com");

        Assertions.assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testBuscarPorPerfil() {
        criarUsuario("Lucas", "lucas@email.com", "MAT008");

        List<UsuarioSummaryDTO> usuarios = usuarioService.buscarPorPerfil("ADMIN");

        Assertions.assertFalse(usuarios.isEmpty());
    }

    @Test
    public void testBuscarPorNomeLikeLimit() {
        for (int i = 0; i < 12; i++) {
            criarUsuario("Usuario Teste " + i, "usuario" + i + "@email.com", "MAT90" + i);
        }

        List<UsuarioSummaryDTO> usuarios = usuarioService.buscarPorNomeLikeLimit("Usuario", 10);

        Assertions.assertFalse(usuarios.isEmpty());
        Assertions.assertEquals(10, usuarios.size());
    }
}
