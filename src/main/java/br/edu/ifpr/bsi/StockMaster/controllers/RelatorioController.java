package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.model.relatorio.RelatorioSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping
    public ResponseEntity<RelatorioSummaryDTO> gerar(
            @RequestHeader("Empresa-Id") Long empresaId) {
        return ResponseEntity.ok(relatorioService.gerar(empresaId));
    }
}