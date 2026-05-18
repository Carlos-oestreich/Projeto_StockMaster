package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoSummaryDTO>> listarTodos(
            @RequestHeader("X-Empresa-Id") Long empresaId) {
        return ResponseEntity.ok(produtoService.listarTodos(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDetailDTO> buscarProdutoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoDetailDTO> cadastrarProduto(
            @RequestBody ProdutoRequestDTO request,
            @RequestHeader("X-Empresa-Id") Long empresaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvar(request, empresaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDetailDTO> atualizarProduto(
            @PathVariable Long id,
            @RequestBody ProdutoRequestDTO request) {
        return ResponseEntity.ok(produtoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}