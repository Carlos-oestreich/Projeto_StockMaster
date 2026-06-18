package br.edu.ifpr.bsi.StockMaster.controllers;

import br.edu.ifpr.bsi.StockMaster.facade.CadastroInicialFacade;
import br.edu.ifpr.bsi.StockMaster.model.cadastroInicial.CadastroInicialRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cadastro-inicial")
public class CadastroInicialController {

    @Autowired
    private CadastroInicialFacade cadastroInicialFacade;

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrar(@RequestBody CadastroInicialRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastroInicialFacade.executar(request));
    }
}