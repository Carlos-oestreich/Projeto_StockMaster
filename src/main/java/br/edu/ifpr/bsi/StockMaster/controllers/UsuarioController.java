package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioSummaryDTO>> listarUsuarios(
            @RequestHeader("Empresa-Id") Long empresaId) {
        return ResponseEntity.ok(usuarioService.listarTodos(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetailDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDetailDTO> cadastrarUsuario(
            @RequestBody UsuarioRequestDTO request,
            @RequestHeader("Empresa-Id") Long empresaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(request, empresaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDetailDTO> atualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}