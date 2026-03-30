package br.edu.ifpr.bsi.projetointegradorprog.repositories;

import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {


    List<Movimentacao> findByTipo(String tipo);

    @Query("SELECT m FROM Movimentacao m WHERE m.quantidade >= :quantidade")
    List<Movimentacao> getAllByQuantidadeMaiorIgual(@Param("quantidade") Integer quantidade);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_movimentacao m WHERE m.tipo_movimentacao = :tipo LIMIT :limit")
    List<Movimentacao> getAllByTipoLimit(@Param("tipo") String tipo, @Param("limit") int limit);

}
