package br.edu.ifpr.bsi.StockMaster.model.relatorio;

import java.math.BigDecimal;

public record ProdutoEstoqueDTO(
        String nome,
        String sku,
        String categoria,
        String fornecedor,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal
) {}