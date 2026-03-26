package br.edu.ifpr.bsi.projetointegradorprog.controllers;

import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.MovimentacaoEstoque;
import br.edu.ifpr.bsi.projetointegradorprog.services.MovimentacaoEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoEstoqueController {

    @Autowired
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoque>> listarMovimentacoes(){
        List<MovimentacaoEstoque> movimentacaoes = this.movimentacaoEstoqueService.listarTodos();
        return ResponseEntity.ok(movimentacaoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoque> buscarMovimentacaoEstoquePorId(@PathVariable Long id){
        MovimentacaoEstoque movimentacao = this.movimentacaoEstoqueService.buscarPorId(id);

        if(movimentacao == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(movimentacao);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarMovimentacaoEstoque(@RequestBody MovimentacaoEstoque request){
        try{
            MovimentacaoEstoque movimentacaoSalva = this.movimentacaoEstoqueService.salvar(request);
            return ResponseEntity.ok(movimentacaoSalva);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMovimentacaoEstoque(@PathVariable Long id, @RequestBody MovimentacaoEstoque request){
        try{
            MovimentacaoEstoque movimentacaoAtualizada = this.movimentacaoEstoqueService.atualizar(id, request);

            if (movimentacaoAtualizada == null){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(movimentacaoAtualizada);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMovimentacaoEstoque(@PathVariable Long id){
        try {
            boolean removido = this.movimentacaoEstoqueService.deletar(id);

            if (!removido){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok("movimentacao removida com sucesso.");
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
