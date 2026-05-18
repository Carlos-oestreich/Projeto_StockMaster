package br.edu.ifpr.bsi.StockMaster.model.categoria;

// DTO de resumo usado principalmente nas listagens.
public record CategoriaSummaryDTO(
        Long id,
        String nome,
        String setor,
        String codigoInterno,
        Boolean ativo
) {
}
