package br.edu.ifpr.bsi.projetointegradorprog.controllers;

import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.Movimentacao;
import br.edu.ifpr.bsi.projetointegradorprog.services.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping
    public ResponseEntity<List<Movimentacao>> listarMovimentacoes(){
        List<Movimentacao> movimentacaoes = this.movimentacaoService.listarTodos();
        return ResponseEntity.ok(movimentacaoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimentacao> buscarMovimentacaoPorId(@PathVariable Long id){
        Movimentacao movimentacao = this.movimentacaoService.buscarPorId(id);

        if(movimentacao == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(movimentacao);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarMovimentacao(@RequestBody Movimentacao request){
        try{
            Movimentacao movimentacaoSalva = this.movimentacaoService.salvar(request);
            return ResponseEntity.ok(movimentacaoSalva);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMovimentacao(@PathVariable Long id, @RequestBody Movimentacao request){
        try{
            Movimentacao movimentacaoAtualizada = this.movimentacaoService.atualizar(id, request);

            if (movimentacaoAtualizada == null){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(movimentacaoAtualizada);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMovimentacao(@PathVariable Long id){
        try {
            boolean removido = this.movimentacaoService.deletar(id);

            if (!removido){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok("movimentacao removida com sucesso.");
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
