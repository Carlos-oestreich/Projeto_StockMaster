package br.edu.ifpr.bsi.StockMaster.model.usuario;

public record UsuarioRequestDTO(
        String nome,
        String email,
        String senha,
        String perfil,
        String matricula,
        String cpf,
        Boolean ativo
) {
}