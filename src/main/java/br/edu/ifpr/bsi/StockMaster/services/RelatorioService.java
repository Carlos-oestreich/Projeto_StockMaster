package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.ProdutoMapper;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.Movimentacao;
import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.relatorio.*;
import br.edu.ifpr.bsi.StockMaster.repositories.MovimentacaoRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private MovimentacaoRepository movimentacaoRepository;
    @Autowired private ProdutoMapper produtoMapper;

    public RelatorioSummaryDTO gerar(Long empresaId) {
        List<Produto> produtos = produtoRepository.findByEmpresaId(empresaId);
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByEmpresaId(empresaId);

        LocalDateTime trintaDiasAtras = LocalDateTime.now().minusDays(30);
        List<Movimentacao> movs30dias = movimentacoes.stream()
                .filter(m -> m.getDataMovimentacao() != null && m.getDataMovimentacao().isAfter(trintaDiasAtras))
                .toList();

        // Valor total do estoque
        BigDecimal valorTotalEstoque = produtos.stream()
                .map(p -> {
                    BigDecimal preco = p.getPreco() == null ? BigDecimal.ZERO : p.getPreco();
                    int qtd = p.getQuantidadeEstoque() == null ? 0 : p.getQuantidadeEstoque();
                    return preco.multiply(BigDecimal.valueOf(qtd));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Entradas e saídas totais
        int qtdEntradas = movimentacoes.stream()
                .filter(m -> "ENTRADA".equalsIgnoreCase(m.getTipo()))
                .mapToInt(m -> m.getQuantidade() == null ? 0 : m.getQuantidade()).sum();

        int qtdSaidas = movimentacoes.stream()
                .filter(m -> "SAIDA".equalsIgnoreCase(m.getTipo()))
                .mapToInt(m -> m.getQuantidade() == null ? 0 : m.getQuantidade()).sum();

        // Valor entradas 30 dias
        BigDecimal valorEntradas30 = movs30dias.stream()
                .filter(m -> "ENTRADA".equalsIgnoreCase(m.getTipo()))
                .map(m -> {
                    BigDecimal preco = m.getProduto() != null && m.getProduto().getPreco() != null ? m.getProduto().getPreco() : BigDecimal.ZERO;
                    return preco.multiply(BigDecimal.valueOf(m.getQuantidade() == null ? 0 : m.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Valor saídas 30 dias
        BigDecimal valorSaidas30 = movs30dias.stream()
                .filter(m -> "SAIDA".equalsIgnoreCase(m.getTipo()))
                .map(m -> {
                    BigDecimal preco = m.getProduto() != null && m.getProduto().getPreco() != null ? m.getProduto().getPreco() : BigDecimal.ZERO;
                    return preco.multiply(BigDecimal.valueOf(m.getQuantidade() == null ? 0 : m.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Top 5 produtos mais vendidos (saídas 30 dias)
        Map<String, int[]> topMap = new HashMap<>();
        Map<String, BigDecimal> topValorMap = new HashMap<>();
        for (Movimentacao m : movs30dias) {
            if (!"SAIDA".equalsIgnoreCase(m.getTipo())) continue;
            String nome = m.getProduto() != null ? m.getProduto().getNome() : "Produto";
            int qtd = m.getQuantidade() == null ? 0 : m.getQuantidade();
            BigDecimal preco = m.getProduto() != null && m.getProduto().getPreco() != null ? m.getProduto().getPreco() : BigDecimal.ZERO;
            topMap.merge(nome, new int[]{qtd}, (a, b) -> new int[]{a[0] + b[0]});
            topValorMap.merge(nome, preco.multiply(BigDecimal.valueOf(qtd)), BigDecimal::add);
        }

        List<TopProdutoSummaryDTO> topProdutos = topMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue()[0], a.getValue()[0]))
                .limit(5)
                .map(e -> new TopProdutoSummaryDTO(e.getKey(), e.getValue()[0], topValorMap.getOrDefault(e.getKey(), BigDecimal.ZERO)))
                .collect(Collectors.toList());

        // Valor vendido por categoria (saídas 30 dias)
        Map<String, BigDecimal> valorCatMap = new HashMap<>();
        for (Movimentacao m : movs30dias) {
            if (!"SAIDA".equalsIgnoreCase(m.getTipo())) continue;
            String cat = m.getProduto() != null && m.getProduto().getCategoria() != null ? m.getProduto().getCategoria().getNome() : "Sem Categoria";
            BigDecimal preco = m.getProduto() != null && m.getProduto().getPreco() != null ? m.getProduto().getPreco() : BigDecimal.ZERO;
            int qtd = m.getQuantidade() == null ? 0 : m.getQuantidade();
            valorCatMap.merge(cat, preco.multiply(BigDecimal.valueOf(qtd)), BigDecimal::add);
        }
        List<ValorCategoriaDTO> valorPorCategoria = valorCatMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .map(e -> new ValorCategoriaDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        // Top produto por categoria (saídas 30 dias)
        Map<String, Map<String, int[]>> catProdMap = new HashMap<>();
        Map<String, Map<String, BigDecimal>> catProdValorMap = new HashMap<>();
        for (Movimentacao m : movs30dias) {
            if (!"SAIDA".equalsIgnoreCase(m.getTipo())) continue;
            String cat = m.getProduto() != null && m.getProduto().getCategoria() != null ? m.getProduto().getCategoria().getNome() : "Sem Categoria";
            String prod = m.getProduto() != null ? m.getProduto().getNome() : "Produto";
            int qtd = m.getQuantidade() == null ? 0 : m.getQuantidade();
            BigDecimal preco = m.getProduto() != null && m.getProduto().getPreco() != null ? m.getProduto().getPreco() : BigDecimal.ZERO;
            catProdMap.computeIfAbsent(cat, k -> new HashMap<>()).merge(prod, new int[]{qtd}, (a, b) -> new int[]{a[0] + b[0]});
            catProdValorMap.computeIfAbsent(cat, k -> new HashMap<>()).merge(prod, preco.multiply(BigDecimal.valueOf(qtd)), BigDecimal::add);
        }
        List<TopProdutoCategoriaDTO> topPorCategoria = catProdMap.entrySet().stream()
                .map(e -> {
                    String cat = e.getKey();
                    Map.Entry<String, int[]> top = e.getValue().entrySet().stream()
                            .max(Comparator.comparingInt(x -> x.getValue()[0])).orElse(null);
                    if (top == null) return null;
                    BigDecimal valor = catProdValorMap.get(cat).getOrDefault(top.getKey(), BigDecimal.ZERO);
                    return new TopProdutoCategoriaDTO(cat, top.getKey(), top.getValue()[0], valor);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Alertas
        List<ProdutoSummaryDTO> alertas = produtos.stream()
                .filter(p -> p.getQuantidadeEstoque() != null && p.getQuantidadeMinima() != null && p.getQuantidadeEstoque() <= p.getQuantidadeMinima())
                .map(produtoMapper::entityToSummaryDTO)
                .collect(Collectors.toList());

        // Posição atual do estoque
        List<ProdutoEstoqueDTO> estoque = produtos.stream()
                .map(p -> {
                    BigDecimal preco = p.getPreco() == null ? BigDecimal.ZERO : p.getPreco();
                    int qtd = p.getQuantidadeEstoque() == null ? 0 : p.getQuantidadeEstoque();
                    return new ProdutoEstoqueDTO(
                            p.getNome(),
                            p.getSku(),
                            p.getCategoria() != null ? p.getCategoria().getNome() : "—",
                            p.getFornecedor() != null ? p.getFornecedor().getNome() : "—",
                            qtd,
                            preco,
                            preco.multiply(BigDecimal.valueOf(qtd))
                    );
                })
                .collect(Collectors.toList());

        return new RelatorioSummaryDTO(
                valorTotalEstoque,
                produtos.size(),
                qtdEntradas,
                qtdSaidas,
                valorEntradas30,
                valorSaidas30,
                topProdutos,
                valorPorCategoria,
                topPorCategoria,
                alertas,
                estoque
        );
    }
}