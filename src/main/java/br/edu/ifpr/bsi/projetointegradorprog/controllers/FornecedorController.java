package br.edu.ifpr.bsi.projetointegradorprog.controllers;

import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.services.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<List<Fornecedor>> listarFornecedores() {
        List<Fornecedor> fornecedores = this.fornecedorService.listarTodos();
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarFornecedorPorId(@PathVariable Long id) {
        Fornecedor fornecedor = this.fornecedorService.buscarPorId(id);

        if (fornecedor == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fornecedor);
    }

    @PostMapping
    public ResponseEntity<Fornecedor> cadastrarFornecedor(@RequestBody Fornecedor request) {
        Fornecedor fornecedorSalvo = this.fornecedorService.salvar(request);
        return ResponseEntity.ok(fornecedorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizarFornecedor(@PathVariable Long id, @RequestBody Fornecedor request) {
        Fornecedor fornecedorAtualizado = this.fornecedorService.atualizar(id, request);

        if (fornecedorAtualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fornecedorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarFornecedor(@PathVariable Long id) {
        boolean removido = this.fornecedorService.deletar(id);

        if (!removido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Fornecedor removido com sucesso.");
    }
}


