package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.empresa.EmpresaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.empresa.EmpresaRequestDTO;
import br.edu.ifpr.bsi.StockMaster.services.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<EmpresaDetailDTO> obterEmpresa(
            @RequestHeader("X-Empresa-Id") Long empresaId) {
        return ResponseEntity.ok(this.empresaService.obter(empresaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDetailDTO> atualizarEmpresa(
            @PathVariable Long id,
            @RequestBody EmpresaRequestDTO request) {
        return ResponseEntity.ok(this.empresaService.atualizar(id, request));
    }
}