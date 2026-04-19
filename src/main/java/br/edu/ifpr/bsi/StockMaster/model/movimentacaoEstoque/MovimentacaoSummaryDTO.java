package br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque;

import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioSummaryDTO;

import java.time.LocalDateTime;

// DTO de resumo usado principalmente nas listagens.
public record MovimentacaoSummaryDTO(
        Long id,
        String tipo,
        Integer quantidade,
        LocalDateTime dataMovimentacao,
        Integer saldoAtual,
        ProdutoSummaryDTO produto,
        UsuarioSummaryDTO usuario
) {
}
