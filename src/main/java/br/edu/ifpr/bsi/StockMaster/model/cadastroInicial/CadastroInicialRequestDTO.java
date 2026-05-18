package br.edu.ifpr.bsi.StockMaster.model.cadastroInicial;

public record CadastroInicialRequestDTO(
        // Empresa
        String nomeEmpresa,
        String cnpjEmpresa,
        String emailEmpresa,
        String telefoneEmpresa,
        // Dono
        String nome,
        String email,
        String senha,
        String cpf,
        String matricula
) {
}
