package br.edu.ifpr.bsi.StockMaster.model.fornecedor;

// DTO de resumo usado principalmente nas listagens.
public record FornecedorSummaryDTO(
        Long id,
        String nome,
        String cnpj,
        String email,
        Boolean ativo
) {
}
