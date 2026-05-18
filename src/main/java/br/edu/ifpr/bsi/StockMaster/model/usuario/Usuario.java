package br.edu.ifpr.bsi.StockMaster.model.usuario;

import br.edu.ifpr.bsi.StockMaster.model.GenericModel;
import br.edu.ifpr.bsi.StockMaster.model.empresa.Empresa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity @Table(name = "tb_usuario")
public class Usuario extends GenericModel {

    @Column(name = "nome_usuario")
    private String nome;
    @Column(name = "email_usuario")
    private String email;
    @Column(name = "senha_usuario")
    private String senha;
    @Column(name = "perfil_usuario")
    private String perfil;
    @Column(name = "matricula_usuario")
    private String matricula;
    @Column(name = "cpf_usuario")
    private String cpf;
    @Column(name = "ativo_usuario")
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}