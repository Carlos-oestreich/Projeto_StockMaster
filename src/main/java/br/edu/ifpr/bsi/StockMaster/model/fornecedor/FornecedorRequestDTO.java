package br.edu.ifpr.bsi.StockMaster.model.fornecedor;

public record FornecedorRequestDTO(
        String nome,
        String cnpj,
        String email,
        String telefone,
        Boolean ativo
) {
}
