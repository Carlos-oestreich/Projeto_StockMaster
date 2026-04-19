package br.edu.ifpr.bsi.StockMaster.model.categoria;

import br.edu.ifpr.bsi.StockMaster.model.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_categoria")
public class Categoria extends GenericModel {

    @Column(name = "nome_categoria")
    private String nome;
    @Column(name = "descricao_categoria")
    private String descricao;
    @Column(name = "setor_categoria")
    private String setor;
    @Column(name = "codigo_interno_categoria")
    private String codigoInterno;
    @Column(name = "ativo_categoria")
    private Boolean ativo;
}
