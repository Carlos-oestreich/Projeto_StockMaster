package br.edu.ifpr.bsi.StockMaster.model.usuario;

import br.edu.ifpr.bsi.StockMaster.model.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_usuario")
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
    @Column(name = "ativo_usuario")
    private Boolean ativo;
}
