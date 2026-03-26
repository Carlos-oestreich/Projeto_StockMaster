package br.edu.ifpr.bsi.projetointegradorprog.services;

import br.edu.ifpr.bsi.projetointegradorprog.model.movimentacaoEstoque.MovimentacaoEstoque;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.model.usuario.Usuario;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.MovimentacaoEstoqueRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.ProdutoRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public MovimentacaoEstoque salvar(MovimentacaoEstoque movimentacaoEstoque){
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
        return movimentacaoEstoqueRepository.save(movimentacaoEstoque);
    }

    public List<MovimentacaoEstoque> listarTodos(){
        return movimentacaoEstoqueRepository.findAll();
    }

    public MovimentacaoEstoque buscarPorId(Long id){
        return movimentacaoEstoqueRepository.findById(id).orElse(null);
    }

    @Transactional
    public MovimentacaoEstoque atualizar(Long id, MovimentacaoEstoque novaMovimentacaoEstoque){
        MovimentacaoEstoque movimentacaoBanco = movimentacaoEstoqueRepository.findById(id).orElse(null);

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

        return movimentacaoEstoqueRepository.save(movimentacaoBanco);
    }

    @Transactional
    public boolean deletar(Long id){
        MovimentacaoEstoque movimentacaoBanco = movimentacaoEstoqueRepository.findById(id).orElse(null);

        if(movimentacaoBanco == null){
            return false;
        }

        Produto produto = produtoRepository.findById(movimentacaoBanco.getProduto().getId()).orElse(null);
        if(produto == null){
            throw new RuntimeException("Produto nao encontrado");
        }

        reverterMovimentacao(produto, movimentacaoBanco.getTipo(), movimentacaoBanco.getQuantidade());

        produtoRepository.save(produto);
        movimentacaoEstoqueRepository.delete(movimentacaoBanco);

        return true;
    }

    public List<MovimentacaoEstoque> buscarPorTipo(String tipo){
        return movimentacaoEstoqueRepository.findByTipo(tipo);
    }

    public List<MovimentacaoEstoque> buscarPorQuantidadeMaiorIgual(Integer quantidade){
        return movimentacaoEstoqueRepository.getAllByQuantidadeMaiorIgual(quantidade);
    }

    public List<MovimentacaoEstoque> buscarPorTipoLimit(String tipo, int limit){
        return movimentacaoEstoqueRepository.getAllByTipoLimit(tipo, limit);
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
