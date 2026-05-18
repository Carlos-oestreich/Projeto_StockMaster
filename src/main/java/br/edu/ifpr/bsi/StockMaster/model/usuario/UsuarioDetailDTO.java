package br.edu.ifpr.bsi.StockMaster.model.usuario;

public record UsuarioDetailDTO(
        Long id,
        String nome,
        String email,
        String perfil,
        String matricula,
        String cpf,
        Boolean ativo
) {
}