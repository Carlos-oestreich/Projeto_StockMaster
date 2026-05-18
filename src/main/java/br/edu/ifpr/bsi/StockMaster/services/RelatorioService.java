package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.ProdutoMapper;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.Movimentacao;
import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.relatorio.RelatorioSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.relatorio.TopProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.MovimentacaoRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public RelatorioSummaryDTO gerar(Long empresaId) {
        List<Produto> produtos = produtoRepository.findByEmpresaId(empresaId);
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByEmpresaId(empresaId);


        BigDecimal valorTotalEstoque = produtos.stream()
                .map(p -> {
                    BigDecimal preco = p.getPreco() == null ? BigDecimal.ZERO : p.getPreco();
                    Integer qtd = p.getQuantidadeEstoque() == null ? 0 : p.getQuantidadeEstoque();
                    return preco.multiply(BigDecimal.valueOf(qtd));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int qtdEntradas = movimentacoes.stream()
                .filter(m -> "ENTRADA".equalsIgnoreCase(m.getTipo()))
                .mapToInt(m -> m.getQuantidade() == null ? 0 : m.getQuantidade())
                .sum();

        int qtdSaidas = movimentacoes.stream()
                .filter(m -> "SAIDA".equalsIgnoreCase(m.getTipo()))
                .mapToInt(m -> m.getQuantidade() == null ? 0 : m.getQuantidade())
                .sum();

        Map<String, Integer> topMap = new HashMap<>();
        for (Movimentacao mov : movimentacoes) {
            String nome = mov.getProduto() != null ? mov.getProduto().getNome() : "Produto";
            int qtd = mov.getQuantidade() == null ? 0 : mov.getQuantidade();
            topMap.merge(nome, qtd, Integer::sum);
        }

        List<TopProdutoSummaryDTO> topProdutos = topMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(e -> new TopProdutoSummaryDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        List<ProdutoSummaryDTO> estoque = produtos.stream()
                .map(produtoMapper::entityToSummaryDTO)
                .collect(Collectors.toList());

        List<ProdutoSummaryDTO> alertas = estoque.stream()
                .filter(p -> p.quantidadeEstoque() != null && p.quantidadeMinima() != null
                        && p.quantidadeEstoque() <= p.quantidadeMinima())
                .collect(Collectors.toList());

        return new RelatorioSummaryDTO(
                valorTotalEstoque,
                produtos.size(),
                qtdEntradas,
                qtdSaidas,
                topProdutos,
                alertas,
                estoque
        );
    }
}