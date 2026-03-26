package br.edu.ifpr.bsi.projetointegradorprog.repositories;

import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {


    List<MovimentacaoEstoque> findByTipo(String tipo);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.quantidade >= :quantidade")
    List<MovimentacaoEstoque> getAllByQuantidadeMaiorIgual(@Param("quantidade") Integer quantidade);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_movimentacao_estoque m WHERE m.tipo_movimentacao = :tipo LIMIT :limit")
    List<MovimentacaoEstoque> getAllByTipoLimit(@Param("tipo") String tipo, @Param("limit") int limit);

}
