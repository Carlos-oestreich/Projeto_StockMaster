package br.edu.ifpr.bsi.StockMaster.model.empresa;

public record EmpresaDetailDTO(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone,
        String endereco,
        String suporte,
        String logo,
        String responsavelNome,
        String responsavelEmail,
        Boolean ativo
) {
}