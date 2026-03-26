package br.edu.ifpr.bsi.projetointegradorprog.repositories;

import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNome(String nome);

    List<Produto> findBySku(String sku);

    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque < p.quantidadeMinima")
    List<Produto> getAllProdutosEstoqueBaixo();

    @Query(nativeQuery = true, value = "SELECT * FROM tb_produto p WHERE p.nome_produto LIKE %:nome% LIMIT :limit")
    List<Produto> getAllByNomeLikeLimit(@Param("nome") String nome, @Param("limit") int limit);

}
