package br.edu.ifpr.bsi.projetointegradorprog.model.produto;

import br.edu.ifpr.bsi.projetointegradorprog.model.GenericModel;
import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tb_produto")
public class Produto extends GenericModel {

    @Column(name = "sku_produto")
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
    @JoinColumn(name = "categoria_profuto")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "fornecedor_produto")
    private Fornecedor fornecedor;


}
