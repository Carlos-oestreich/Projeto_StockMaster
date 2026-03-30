package br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque;

import br.edu.ifpr.bsi.projetointegradorprog.model.GenericModel;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tb_movimentacao")
public class Movimentacao extends GenericModel {

    @Column(name = "tipo_movimentacao")
    private String tipo;
    @Column(name = "quantidade_movimentacao")
    private Integer quantidade;
    @Column(name = "observacao_movimentacao")
    private String observacao;
    @Column(name = "data_movimentacao")
    private LocalDateTime dataMovimentacao;
    @Column(name = "saldo_anterior_movimentacao")
    private Integer saldoAnterior;
    @Column(name = "saldo_atual_movimentacao")
    private Integer saldoAtual;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
