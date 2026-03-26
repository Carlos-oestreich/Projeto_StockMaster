package br.edu.ifpr.bsi.projetointegradorprog.controllers;

import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos(){
        List<Produto> produtos = this.produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id){
        Produto produto = this.produtoService.buscarPorId(id);

        if(produto == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarProduto(@RequestBody Produto request){
        try {
            Produto produtoSalvo = this.produtoService.salvar(request);
            return ResponseEntity.ok(produtoSalvo);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody Produto request){
        try{
            Produto produtoAtualizado = this.produtoService.atualizar(id, request);

            if(produtoAtualizado == null){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarProduto(@PathVariable Long id){
        boolean removido = this.produtoService.deletar(id);

        if(!removido){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Produto removido com sucesso!");
    }
}
