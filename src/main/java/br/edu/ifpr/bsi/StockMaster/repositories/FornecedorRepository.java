package br.edu.ifpr.bsi.StockMaster.repositories;

import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long>   {

    List<Fornecedor> findByNome(String nome);

    List<Fornecedor> findByCnpj(String cnpj);

    @Query("SELECT f FROM Fornecedor f WHERE f.nome LIKE %:nome%")
    List<Fornecedor> getAllByNomeLike(@Param("nome") String nome);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_fornecedor f WHERE f.ativo_fornecedor = :ativo LIMIT :limit")
    List<Fornecedor> getAllByAtivoLimit(@Param("ativo") boolean ativo, @Param("limit") int limit);

}
