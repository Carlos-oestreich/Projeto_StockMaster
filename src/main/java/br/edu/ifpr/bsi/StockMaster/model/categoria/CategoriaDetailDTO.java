package br.edu.ifpr.bsi.StockMaster.model.categoria;

// DTO detalhado usado na resposta principal da categoria.
public record CategoriaDetailDTO(
        Long id,
        String nome,
        String descricao,
        String setor,
        String codigoInterno,
        Boolean ativo
) {
}
