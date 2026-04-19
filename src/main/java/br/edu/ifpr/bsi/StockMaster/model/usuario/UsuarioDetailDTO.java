package br.edu.ifpr.bsi.StockMaster.model.usuario;

// DTO detalhado usado na resposta principal do usuario.
public record UsuarioDetailDTO(
        Long id,
        String nome,
        String email,
        String perfil,
        String matricula,
        Boolean ativo
) {
}
