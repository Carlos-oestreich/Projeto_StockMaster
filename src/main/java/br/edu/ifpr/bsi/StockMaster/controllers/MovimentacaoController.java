package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.EmpresaRepository;
import br.edu.ifpr.bsi.StockMaster.services.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;
    @Autowired private EmpresaRepository empresaRepository;


    @GetMapping
    public ResponseEntity<List<MovimentacaoSummaryDTO>> listarMovimentacoes(
            @RequestHeader("X-Empresa-Id") Long empresaId) {
        return ResponseEntity.ok(movimentacaoService.listarTodos(empresaId));
    }

    @PostMapping
    public ResponseEntity<MovimentacaoDetailDTO> cadastrarMovimentacao(
            @RequestBody MovimentacaoRequestDTO request,
            @RequestHeader("X-Empresa-Id") Long empresaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoService.salvar(request, empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoDetailDTO> buscarMovimentacaoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimentacaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoDetailDTO> atualizarMovimentacao(
            @PathVariable Long id,
            @RequestBody MovimentacaoRequestDTO request) {
        return ResponseEntity.ok(movimentacaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMovimentacao(@PathVariable Long id) {
        movimentacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}