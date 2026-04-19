package br.edu.ifpr.bsi.StockMaster.model.usuario;

// DTO de resumo usado principalmente nas listagens.
public record UsuarioSummaryDTO(
        Long id,
        String nome,
        String email,
        String perfil,
        Boolean ativo
) {
}
