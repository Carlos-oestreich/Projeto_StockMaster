package br.edu.ifpr.bsi.StockMaster.mappers;

import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class, FornecedorMapper.class, UsuarioMapper.class})
public interface ProdutoMapper {

    @Mapping(target = "categoria", source = "categoriaId", qualifiedByName = "categoriaFromId")
    @Mapping(target = "fornecedor", source = "fornecedorId", qualifiedByName = "fornecedorFromId")
    Produto requestDTOToEntity(ProdutoRequestDTO produtoRequestDTO);

    ProdutoDetailDTO entityToDetailDTO(Produto produto);

    ProdutoSummaryDTO entityToSummaryDTO(Produto produto);

    @Named("categoriaFromId")
    default Categoria categoriaFromId(Long id) {
        if (id == null) {
            return null;
        }

        Categoria categoria = new Categoria();
        categoria.setId(id);
        return categoria;
    }

    @Named("fornecedorFromId")
    default Fornecedor fornecedorFromId(Long id) {
        if (id == null) {
            return null;
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(id);
        return fornecedor;
    }

}
