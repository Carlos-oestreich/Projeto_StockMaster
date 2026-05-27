package br.edu.ifpr.bsi.StockMaster.model.relatorio;

import java.math.BigDecimal;

public record TopProdutoSummaryDTO(String nome, Integer quantidade, BigDecimal valor) {}