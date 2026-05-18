package br.edu.ifpr.bsi.StockMaster.repositories;

import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByEmpresaId(Long empresaId);
    List<Produto> findByNomeAndEmpresaId(String nome, Long empresaId);
    Optional<Produto> findBySkuAndEmpresaId(String sku, Long empresaId);

    @Query("SELECT p FROM Produto p WHERE p.empresa.id = :empresaId AND p.quantidadeEstoque < p.quantidadeMinima")
    List<Produto> getAllProdutosEstoqueBaixoByEmpresaId(@Param("empresaId") Long empresaId);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_produto p WHERE p.nome_produto LIKE %:nome% AND p.empresa_id = :empresaId LIMIT :limit")
    List<Produto> getAllByNomeLikeLimitAndEmpresaId(@Param("nome") String nome, @Param("limit") int limit, @Param("empresaId") Long empresaId);
}