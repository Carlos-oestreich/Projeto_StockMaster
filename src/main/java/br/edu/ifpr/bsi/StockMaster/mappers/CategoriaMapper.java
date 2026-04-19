package br.edu.ifpr.bsi.StockMaster.mappers;

import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.categoria.CategoriaSummaryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    Categoria requestDTOToEntity(CategoriaRequestDTO categoriaRequestDTO);

    CategoriaDetailDTO entityToDetailDTO(Categoria categoria);

    CategoriaSummaryDTO entityToSummaryDTO(Categoria categoria);

}
