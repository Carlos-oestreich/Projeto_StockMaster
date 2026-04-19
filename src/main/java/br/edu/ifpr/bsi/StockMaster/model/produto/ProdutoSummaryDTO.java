package br.edu.ifpr.bsi.StockMaster.model.produto;

import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorSummaryDTO;

import java.math.BigDecimal;

// DTO de resumo usado principalmente nas listagens.
public record ProdutoSummaryDTO(
        Long id,
        String sku,
        String nome,
        BigDecimal preco,
        String marca,
        Integer quantidadeEstoque,
        CategoriaSummaryDTO categoria,
        FornecedorSummaryDTO fornecedor
) {
}
