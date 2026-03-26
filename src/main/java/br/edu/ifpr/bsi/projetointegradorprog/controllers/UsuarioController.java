package br.edu.ifpr.bsi.projetointegradorprog.controllers;

import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = this.usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario request) {
        Usuario usuarioSalvo = usuarioService.salvar(request);
        return ResponseEntity.ok(usuarioSalvo);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario request) {
        Usuario usuarioAtualizado = this.usuarioService.atualizar(id, request);

        if (usuarioAtualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        boolean removido = this.usuarioService.deletar(id);

        if (!removido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Usuario removido com sucesso!");
    }

}
