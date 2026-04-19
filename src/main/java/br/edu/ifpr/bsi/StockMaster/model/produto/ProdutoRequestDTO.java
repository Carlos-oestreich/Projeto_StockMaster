package br.edu.ifpr.bsi.StockMaster.model.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoRequestDTO(
        String sku,
        String nome,
        String descricao,
        BigDecimal preco,
        String marca,
        Integer quantidadeEstoque,
        Integer quantidadeMinima,
        LocalDateTime dataCadastro,
        Long categoriaId,
        Long fornecedorId
) {
}
