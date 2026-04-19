package br.edu.ifpr.bsi.StockMaster.model.fornecedor;

import br.edu.ifpr.bsi.StockMaster.model.GenericModel;
import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tb_fornecedor")
public class Fornecedor extends GenericModel {

    @Column(name = "nome_fornecedor")
    private String nome;
    @Column(name = "cnpj_fornecedor")
    private String cnpj;
    @Column(name = "email_fornecedor")
    private String email;
    @Column(name = "telefone_fornecedor")
    private String telefone;
    @Column(name = "ativo_fornecedor")
    private Boolean ativo;

    @OneToMany(mappedBy = "fornecedor")
    @JsonIgnore
    private List<Produto> produtos = new ArrayList<>();

}
