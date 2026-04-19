package br.edu.ifpr.bsi.StockMaster.repositories;

import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByNome(String nome);

    @Query("SELECT c FROM Categoria c WHERE c.setor LIKE %:setor%")
    List<Categoria> getAllBySetorLike(@Param("setor") String setor);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_categoria c WHERE c.nome_categoria LIKE %:nome% LIMIT :limit")
    List<Categoria> getAllByNomeLikeLimit(@Param("nome") String nome, @Param("limit") int limit);

}
