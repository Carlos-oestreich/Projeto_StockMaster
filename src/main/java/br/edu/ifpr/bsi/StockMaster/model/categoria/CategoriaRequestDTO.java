package br.edu.ifpr.bsi.StockMaster.model.categoria;

public record CategoriaRequestDTO(
        String nome,
        String descricao,
        String setor,
        String codigoInterno,
        Boolean ativo
) {
}
