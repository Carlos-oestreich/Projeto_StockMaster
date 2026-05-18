package br.edu.ifpr.bsi.StockMaster.model.empresa;

import br.edu.ifpr.bsi.StockMaster.model.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_empresa")
public class Empresa extends GenericModel {

    @Column(name = "nome_empresa")
    private String nome;

    @Column(name = "cnpj_empresa")
    private String cnpj;

    @Column(name = "email_empresa")
    private String email;

    @Column(name = "telefone_empresa")
    private String telefone;

    @Column(name = "endereco_empresa")
    private String endereco;

    @Column(name = "suporte_empresa")
    private String suporte;

    @Column(name = "logo_empresa")
    private String logo;

    @Column(name = "ativo_empresa")
    private Boolean ativo = true;
}