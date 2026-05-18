package br.edu.ifpr.bsi.StockMaster.model.relatorio;

import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import java.math.BigDecimal;
import java.util.List;

public record RelatorioSummaryDTO(
        BigDecimal valorTotalEstoque,
        Integer totalSkus,
        Integer qtdEntradas,
        Integer qtdSaidas,
        List<TopProdutoSummaryDTO> topProdutos,
        List<ProdutoSummaryDTO> alertas,
        List<ProdutoSummaryDTO> estoque
) {
}