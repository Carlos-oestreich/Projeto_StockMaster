package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.empresa.EmpresaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.empresa.EmpresaRequestDTO;
import br.edu.ifpr.bsi.StockMaster.services.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/empresas")
@CrossOrigin(origins = "http://localhost:5173")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<EmpresaDetailDTO> obterEmpresa(
            @RequestHeader("Empresa-Id") Long empresaId) {
        return ResponseEntity.ok(this.empresaService.obter(empresaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDetailDTO> atualizarEmpresa(
            @PathVariable Long id,
            @RequestBody EmpresaRequestDTO request) {
        return ResponseEntity.ok(this.empresaService.atualizar(id, request));
    }

    @PostMapping(value = "/{id}/logo", consumes = "multipart/form-data")
    public ResponseEntity<EmpresaDetailDTO> uploadLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(empresaService.atualizarLogo(id, file));
    }
}