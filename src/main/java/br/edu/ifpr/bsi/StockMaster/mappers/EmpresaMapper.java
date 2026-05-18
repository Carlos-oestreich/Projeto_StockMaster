package br.edu.ifpr.bsi.StockMaster.mappers;

import br.edu.ifpr.bsi.StockMaster.model.empresa.Empresa;
import br.edu.ifpr.bsi.StockMaster.model.empresa.EmpresaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.empresa.EmpresaRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    Empresa requestDTOToEntity(EmpresaRequestDTO dto);
    EmpresaDetailDTO entityToDetailDTO(Empresa entity);
}