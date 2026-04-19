package br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque;

import java.time.LocalDateTime;

public record MovimentacaoRequestDTO(
        String tipo,
        Integer quantidade,
        String observacao,
        LocalDateTime dataMovimentacao,
        Long produtoId,
        Long usuarioId
) {
}
