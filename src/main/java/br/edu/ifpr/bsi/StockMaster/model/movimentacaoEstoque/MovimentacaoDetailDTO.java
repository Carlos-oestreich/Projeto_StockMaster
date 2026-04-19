package br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque;

import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioSummaryDTO;

import java.time.LocalDateTime;

// DTO detalhado usado na resposta principal da movimentacao.
public record MovimentacaoDetailDTO(
        Long id,
        String tipo,
        Integer quantidade,
        String observacao,
        LocalDateTime dataMovimentacao,
        Integer saldoAnterior,
        Integer saldoAtual,
        ProdutoSummaryDTO produto,
        UsuarioSummaryDTO usuario
) {
}
