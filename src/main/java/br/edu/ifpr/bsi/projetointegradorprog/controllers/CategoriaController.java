package br.edu.ifpr.bsi.projetointegradorprog.controllers;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = this.categoriaService.listarTodos();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = this.categoriaService.buscarPorId(id);

        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<Categoria> cadastrarCategoria(@RequestBody Categoria request) {
        Categoria categoriaSalva = this.categoriaService.salvar(request);
        return ResponseEntity.ok(categoriaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Long id, @RequestBody Categoria request) {
        Categoria categoriaAtualizada = this.categoriaService.atualizar(id, request);

        if (categoriaAtualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categoriaAtualizada);
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<String>  deletarCategoria(@PathVariable Long id) {
        boolean removido = this.categoriaService.deletar(id);

        if(!removido){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Categoria removida com sucesso!");
    }
}
