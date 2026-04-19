package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.services.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping
    public ResponseEntity<List<MovimentacaoSummaryDTO>> listarMovimentacoes() {
        return ResponseEntity.ok(this.movimentacaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoDetailDTO> buscarMovimentacaoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(this.movimentacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MovimentacaoDetailDTO> cadastrarMovimentacao(@RequestBody MovimentacaoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.movimentacaoService.salvar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoDetailDTO> atualizarMovimentacao(@PathVariable Long id, @RequestBody MovimentacaoRequestDTO request) {
        return ResponseEntity.ok(this.movimentacaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMovimentacao(@PathVariable Long id) {
        this.movimentacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
