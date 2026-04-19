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

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoSummaryDTO>> listarTodos() {
        return ResponseEntity.ok(this.produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDetailDTO> buscarProdutoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(this.produtoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoDetailDTO> cadastrarProduto(@RequestBody ProdutoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.produtoService.salvar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDetailDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequestDTO request) {
        return ResponseEntity.ok(this.produtoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        this.produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
