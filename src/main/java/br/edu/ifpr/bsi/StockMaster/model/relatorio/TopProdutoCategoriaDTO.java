package br.edu.ifpr.bsi.StockMaster.model.relatorio;

import java.math.BigDecimal;

public record TopProdutoCategoriaDTO(String categoria, String produto, Integer quantidade, BigDecimal valor) {}