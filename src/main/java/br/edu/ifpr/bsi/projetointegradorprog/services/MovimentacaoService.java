package br.edu.ifpr.bsi.projetointegradorprog.services;

import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.Movimentacao;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.MovimentacaoRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.ProdutoRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Movimentacao salvar(Movimentacao movimentacaoEstoque){
        Produto produto = produtoRepository.findById(movimentacaoEstoque.getProduto().getId()).orElse(null);
        Usuario usuario = usuarioRepository.findById(movimentacaoEstoque.getUsuario().getId()).orElse(null);

        if(produto == null){
            throw new RuntimeException("Produto não encontrado");
        }

        if(usuario == null){
            throw new RuntimeException("Usuario não encontrado");
        }

        Integer saldoAnterior = produto.getQuantidadeEstoque();

        aplicarMovimentacao(produto, movimentacaoEstoque.getTipo(), movimentacaoEstoque.getQuantidade());

        movimentacaoEstoque.setProduto(produto);
        movimentacaoEstoque.setUsuario(usuario);
        movimentacaoEstoque.setSaldoAnterior(saldoAnterior);
        movimentacaoEstoque.setSaldoAtual(produto.getQuantidadeEstoque());

        if (movimentacaoEstoque.getDataMovimentacao() == null) {
            movimentacaoEstoque.setDataMovimentacao(LocalDateTime.now());
        }

        produtoRepository.save(produto);
        return movimentacaoRepository.save(movimentacaoEstoque);
    }

    public List<Movimentacao> listarTodos(){
        return movimentacaoRepository.findAll();
    }

    public Movimentacao buscarPorId(Long id){
        return movimentacaoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Movimentacao atualizar(Long id, Movimentacao novaMovimentacaoEstoque){
        Movimentacao movimentacaoBanco = movimentacaoRepository.findById(id).orElse(null);

        if(movimentacaoBanco == null){
            return null;
        }

        Produto produtoAntigo = produtoRepository.findById(movimentacaoBanco.getProduto().getId()).orElse(null);
        if(produtoAntigo == null){
            throw new RuntimeException("Produto da movimentacao original não encontrado");
        }

        Usuario usuarioNovo = usuarioRepository.findById(novaMovimentacaoEstoque.getUsuario().getId()).orElse(null);
        if(usuarioNovo == null){
            throw new RuntimeException("Usuario nao encontrado");
        }

        Produto produtoNovo = produtoRepository.findById(novaMovimentacaoEstoque.getProduto().getId()).orElse(null);
        if(produtoNovo == null){
            throw new RuntimeException("Produto nao encontrado");
        }

        reverterMovimentacao(produtoAntigo, movimentacaoBanco.getTipo(), movimentacaoBanco.getQuantidade());
        produtoRepository.save(produtoAntigo);

        Integer saldoAnteriorNovo = produtoNovo.getQuantidadeEstoque();

        aplicarMovimentacao(produtoNovo, novaMovimentacaoEstoque.getTipo(), novaMovimentacaoEstoque.getQuantidade());
        produtoRepository.save(produtoNovo);

        movimentacaoBanco.setTipo(novaMovimentacaoEstoque.getTipo());
        movimentacaoBanco.setQuantidade(novaMovimentacaoEstoque.getQuantidade());
        movimentacaoBanco.setObservacao(novaMovimentacaoEstoque.getObservacao());

        if (novaMovimentacaoEstoque.getDataMovimentacao() != null) {
            movimentacaoBanco.setDataMovimentacao(novaMovimentacaoEstoque.getDataMovimentacao());
        }

        movimentacaoBanco.setProduto(produtoNovo);
        movimentacaoBanco.setUsuario(usuarioNovo);
        movimentacaoBanco.setSaldoAnterior(saldoAnteriorNovo);
        movimentacaoBanco.setSaldoAtual(produtoNovo.getQuantidadeEstoque());

        return movimentacaoRepository.save(movimentacaoBanco);
    }

    @Transactional
    public boolean deletar(Long id){
        Movimentacao movimentacaoBanco = movimentacaoRepository.findById(id).orElse(null);

        if(movimentacaoBanco == null){
            return false;
        }

        Produto produto = produtoRepository.findById(movimentacaoBanco.getProduto().getId()).orElse(null);
        if(produto == null){
            throw new RuntimeException("Produto nao encontrado");
        }

        reverterMovimentacao(produto, movimentacaoBanco.getTipo(), movimentacaoBanco.getQuantidade());

        produtoRepository.save(produto);
        movimentacaoRepository.delete(movimentacaoBanco);

        return true;
    }

    public List<Movimentacao> buscarPorTipo(String tipo){
        return movimentacaoRepository.findByTipo(tipo);
    }

    public List<Movimentacao> buscarPorQuantidadeMaiorIgual(Integer quantidade){
        return movimentacaoRepository.getAllByQuantidadeMaiorIgual(quantidade);
    }

    public List<Movimentacao> buscarPorTipoLimit(String tipo, int limit){
        return movimentacaoRepository.getAllByTipoLimit(tipo, limit);
    }

    private void aplicarMovimentacao(Produto produto, String tipo, Integer quantidade){
        validarTipoEQuantidade(tipo, quantidade);

        if(tipo.equalsIgnoreCase("ENTRADA")) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        } else if(tipo.equalsIgnoreCase("SAIDA")) {
            if (produto.getQuantidadeEstoque() < quantidade) {
                throw new RuntimeException("Quantidade de estoque insuficiente");
            }
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        }
    }

    private void reverterMovimentacao(Produto produto, String tipo, Integer quantidade){
        validarTipoEQuantidade(tipo, quantidade);

        if(tipo.equalsIgnoreCase("ENTRADA")) {
            if(produto.getQuantidadeEstoque() < quantidade) {
                throw new RuntimeException("nao e possivel reverter a entrada");
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        } else if(tipo.equalsIgnoreCase("SAIDA")) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        }
    }

    private void validarTipoEQuantidade(String tipo, Integer quantidade){
        if (tipo == null || (!tipo.equalsIgnoreCase("ENTRADA") && !tipo.equalsIgnoreCase("SAIDA"))) {
            throw new RuntimeException("Tipo de movimentacao invalido.");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new RuntimeException("Quantidade invalida.");
        }
    }

}
