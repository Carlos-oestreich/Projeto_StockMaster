package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CategoriaController {

    @Autowired private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaSummaryDTO>> listar(@RequestHeader("X-Empresa-Id") Long empresaId) {
        return ResponseEntity.ok(categoriaService.listarTodos(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDetailDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaDetailDTO> salvar(@RequestBody CategoriaRequestDTO request,
                                                     @RequestHeader("X-Empresa-Id") Long empresaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.salvar(request, empresaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDetailDTO> atualizar(@PathVariable Long id,
                                                        @RequestBody CategoriaRequestDTO request) {
        return ResponseEntity.ok(categoriaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}