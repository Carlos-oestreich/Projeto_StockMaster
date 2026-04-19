package br.edu.ifpr.bsi.StockMaster.model.produto;

import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoSummaryDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// DTO detalhado usado na resposta principal do produto.
public record ProdutoDetailDTO(
        Long id,
        String sku,
        String nome,
        String descricao,
        BigDecimal preco,
        String marca,
        Integer quantidadeEstoque,
        Integer quantidadeMinima,
        LocalDateTime dataCadastro,
        CategoriaSummaryDTO categoria,
        FornecedorSummaryDTO fornecedor,
        List<MovimentacaoSummaryDTO> movimentacoes
) {
}
