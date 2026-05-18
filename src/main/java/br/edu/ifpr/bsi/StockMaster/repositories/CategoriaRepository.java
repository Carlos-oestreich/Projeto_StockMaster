package br.edu.ifpr.bsi.StockMaster.repositories;

import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByEmpresaId(Long empresaId);
    List<Categoria> findByNomeAndEmpresaId(String nome, Long empresaId);

    @Query("SELECT c FROM Categoria c WHERE c.empresa.id = :empresaId AND c.setor LIKE %:setor%")
    List<Categoria> getAllBySetorLikeAndEmpresaId(@Param("setor") String setor, @Param("empresaId") Long empresaId);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_categoria c WHERE c.nome_categoria LIKE %:nome% AND c.empresa_id = :empresaId LIMIT :limit")
    List<Categoria> getAllByNomeLikeLimitAndEmpresaId(@Param("nome") String nome, @Param("limit") int limit, @Param("empresaId") Long empresaId);
}