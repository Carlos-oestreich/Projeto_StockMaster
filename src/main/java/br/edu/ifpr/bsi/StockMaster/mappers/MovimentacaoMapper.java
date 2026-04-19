package br.edu.ifpr.bsi.StockMaster.mappers;

import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.Movimentacao;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class, UsuarioMapper.class})
public interface MovimentacaoMapper {

    @Mapping(target = "produto", source = "produtoId", qualifiedByName = "produtoFromId")
    @Mapping(target = "usuario", source = "usuarioId", qualifiedByName = "usuarioFromId")
    Movimentacao requestDTOToEntity(MovimentacaoRequestDTO movimentacaoRequestDTO);

    MovimentacaoDetailDTO entityToDetailDTO(Movimentacao movimentacao);

    MovimentacaoSummaryDTO entityToSummaryDTO(Movimentacao movimentacao);

    @Named("produtoFromId")
    default Produto produtoFromId(Long id) {
        if (id == null) {
            return null;
        }

        Produto produto = new Produto();
        produto.setId(id);
        return produto;
    }

    @Named("usuarioFromId")
    default Usuario usuarioFromId(Long id) {
        if (id == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(id);
        return usuario;
    }

}
