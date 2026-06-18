package br.edu.ifpr.bsi.StockMaster.model.relatorio;

import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import java.math.BigDecimal;
import java.util.List;

public record RelatorioSummaryDTO(
        BigDecimal valorTotalEstoque,
        Integer totalSkus,
        Integer qtdEntradas,
        Integer qtdSaidas,
        BigDecimal valorEntradas30dias,
        BigDecimal valorSaidas30dias,
        List<TopProdutoSummaryDTO> topProdutos,
        List<ValorCategoriaDTO> valorPorCategoria,
        List<TopProdutoCategoriaDTO> topPorCategoria,
        List<ProdutoSummaryDTO> alertas,
        List<ProdutoEstoqueDTO> estoque
) {}