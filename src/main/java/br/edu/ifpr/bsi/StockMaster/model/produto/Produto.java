package br.edu.ifpr.bsi.StockMaster.model.produto;

import br.edu.ifpr.bsi.StockMaster.model.GenericModel;
import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.empresa.Empresa;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.Movimentacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Entity @Table(name = "tb_produto")
public class Produto extends GenericModel {

    @Column(name = "sku_produto", unique = true, nullable = false)
    private String sku;
    @Column(name = "nome_produto")
    private String nome;
    @Column(name = "descricao_produto")
    private String descricao;
    @Column(name = "preco_produto")
    private BigDecimal preco;
    @Column(name = "marca_produto")
    private String marca;
    @Column(name = "quantidade_estoque_produto")
    private Integer quantidadeEstoque;
    @Column(name = "quantidade_minima_produto")
    private Integer quantidadeMinima;
    @Column(name = "data_cadastro_produto")
    private LocalDateTime dataCadastro;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<Movimentacao> movimentacoes = new ArrayList<>();
}