package br.edu.ifpr.bsi.StockMaster.repositories;

import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByEmpresaId(Long empresaId);
    List<Movimentacao> findByProdutoIdAndEmpresaId(Long produtoId, Long empresaId);
    List<Movimentacao> findByTipo(String tipo);


}