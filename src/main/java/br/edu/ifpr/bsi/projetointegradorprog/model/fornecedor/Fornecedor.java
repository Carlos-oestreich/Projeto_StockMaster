package br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor;

import br.edu.ifpr.bsi.projetointegradorprog.model.GenericModel;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private List<Produto> produtos;
}
