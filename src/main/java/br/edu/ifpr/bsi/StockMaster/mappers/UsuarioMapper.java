package br.edu.ifpr.bsi.StockMaster.mappers;

import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.UsuarioSummaryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario requestDTOToEntity(UsuarioRequestDTO usuarioRequestDTO);

    UsuarioDetailDTO entityToDetailDTO(Usuario usuario);

    UsuarioSummaryDTO entityToSummaryDTO(Usuario usuario);

}
