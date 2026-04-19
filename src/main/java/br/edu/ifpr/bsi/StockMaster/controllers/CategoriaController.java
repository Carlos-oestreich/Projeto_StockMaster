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
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaSummaryDTO>> listarCategorias() {
        return ResponseEntity.ok(this.categoriaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDetailDTO> buscarCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(this.categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaDetailDTO> cadastrarCategoria(@RequestBody CategoriaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoriaService.salvar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDetailDTO> atualizarCategoria(@PathVariable Long id, @RequestBody CategoriaRequestDTO request) {
        return ResponseEntity.ok(this.categoriaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        this.categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
