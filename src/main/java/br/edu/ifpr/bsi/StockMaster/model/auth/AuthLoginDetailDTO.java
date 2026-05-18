package br.edu.ifpr.bsi.StockMaster.model.auth;

public record AuthLoginDetailDTO(
        Long id,
        String nome,
        String email,
        String perfil,
        String cpf,
        Long empresaId,
        String token
) {}