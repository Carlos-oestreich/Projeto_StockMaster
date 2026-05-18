package br.edu.ifpr.bsi.StockMaster.model.empresa;

public record EmpresaRequestDTO(
        String nome,
        String cnpj,
        String email,
        String telefone,
        String endereco,
        String suporte,
        String logo
) {
}