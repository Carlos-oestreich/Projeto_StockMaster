package br.edu.ifpr.bsi.StockMaster.repositories;

import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    List<Fornecedor> findByEmpresaId(Long empresaId);
    List<Fornecedor> findByNomeAndEmpresaId(String nome, Long empresaId);
    List<Fornecedor> findByCnpjAndEmpresaId(String cnpj, Long empresaId);

    @Query("SELECT f FROM Fornecedor f WHERE f.empresa.id = :empresaId AND f.nome LIKE %:nome%")
    List<Fornecedor> getAllByNomeLikeAndEmpresaId(@Param("nome") String nome, @Param("empresaId") Long empresaId);
}