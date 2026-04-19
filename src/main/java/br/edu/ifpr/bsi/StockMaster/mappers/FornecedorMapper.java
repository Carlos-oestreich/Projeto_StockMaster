package br.edu.ifpr.bsi.StockMaster.mappers;

import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.FornecedorSummaryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class})
public interface FornecedorMapper {

    Fornecedor requestDTOToEntity(FornecedorRequestDTO fornecedorRequestDTO);

    FornecedorDetailDTO entityToDetailDTO(Fornecedor fornecedor);

    FornecedorSummaryDTO entityToSummaryDTO(Fornecedor fornecedor);

}
