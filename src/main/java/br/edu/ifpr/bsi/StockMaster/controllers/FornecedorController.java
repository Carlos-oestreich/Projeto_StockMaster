package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.services.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<List<FornecedorSummaryDTO>> listarFornecedores(
            @RequestHeader("Empresa-Id") Long empresaId) {
        return ResponseEntity.ok(fornecedorService.listarTodos(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDetailDTO> buscarFornecedorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FornecedorDetailDTO> cadastrarFornecedor(
            @RequestBody FornecedorRequestDTO request,
            @RequestHeader("Empresa-Id") Long empresaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.salvar(request, empresaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDetailDTO> atualizarFornecedor(
            @PathVariable Long id,
            @RequestBody FornecedorRequestDTO request) {
        return ResponseEntity.ok(fornecedorService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable Long id) {
        fornecedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}