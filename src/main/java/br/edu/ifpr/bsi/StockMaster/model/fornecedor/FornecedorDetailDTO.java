package br.edu.ifpr.bsi.StockMaster.model.fornecedor;

import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;

import java.util.List;

// DTO detalhado usado na resposta principal do fornecedor.
public record FornecedorDetailDTO(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone,
        Boolean ativo,
        List<ProdutoSummaryDTO> produtos
) {
}
