package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.MovimentacaoMapper;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.Movimentacao;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.movimentacaoEstoque.MovimentacaoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.repositories.MovimentacaoRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.ProdutoRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private MovimentacaoMapper movimentacaoMapper;

    @Transactional
    public MovimentacaoDetailDTO salvar(MovimentacaoRequestDTO request) {
        Movimentacao movimentacaoEstoque = this.movimentacaoMapper.requestDTOToEntity(request);
        Produto produto = buscarProduto(movimentacaoEstoque);
        Usuario usuario = buscarUsuario(movimentacaoEstoque);

        Integer saldoAnterior = produto.getQuantidadeEstoque();

        aplicarMovimentacao(produto, movimentacaoEstoque.getTipo(), movimentacaoEstoque.getQuantidade());

        movimentacaoEstoque.setProduto(produto);
        movimentacaoEstoque.setUsuario(usuario);
        movimentacaoEstoque.setSaldoAnterior(saldoAnterior);
        movimentacaoEstoque.setSaldoAtual(produto.getQuantidadeEstoque());

        if (movimentacaoEstoque.getDataMovimentacao() == null) {
            movimentacaoEstoque.setDataMovimentacao(LocalDateTime.now());
        }

        this.produtoRepository.save(produto);
        return this.movimentacaoMapper.entityToDetailDTO(this.movimentacaoRepository.save(movimentacaoEstoque));
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoSummaryDTO> listarTodos() {
        return this.movimentacaoRepository.findAll()
                .stream()
                .map(this.movimentacaoMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimentacaoDetailDTO buscarPorId(Long id) {
        return this.movimentacaoMapper.entityToDetailDTO(buscarEntidade(id));
    }

    @Transactional
    public MovimentacaoDetailDTO atualizar(Long id, MovimentacaoRequestDTO request) {
        Movimentacao novaMovimentacaoEstoque = this.movimentacaoMapper.requestDTOToEntity(request);
        Movimentacao movimentacaoBanco = buscarEntidade(id);

        Produto produtoAntigo = buscarProdutoPorId(movimentacaoBanco.getProduto().getId());
        Usuario usuarioNovo = buscarUsuario(novaMovimentacaoEstoque);
        Produto produtoNovo = buscarProduto(novaMovimentacaoEstoque);

        reverterMovimentacao(produtoAntigo, movimentacaoBanco.getTipo(), movimentacaoBanco.getQuantidade());
        this.produtoRepository.save(produtoAntigo);

        Integer saldoAnteriorNovo = produtoNovo.getQuantidadeEstoque();

        aplicarMovimentacao(produtoNovo, novaMovimentacaoEstoque.getTipo(), novaMovimentacaoEstoque.getQuantidade());
        this.produtoRepository.save(produtoNovo);

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

        return this.movimentacaoMapper.entityToDetailDTO(this.movimentacaoRepository.save(movimentacaoBanco));
    }

    @Transactional
    public void deletar(Long id) {
        Movimentacao movimentacaoBanco = buscarEntidade(id);
        Produto produto = buscarProdutoPorId(movimentacaoBanco.getProduto().getId());

        reverterMovimentacao(produto, movimentacaoBanco.getTipo(), movimentacaoBanco.getQuantidade());

        this.produtoRepository.save(produto);
        this.movimentacaoRepository.delete(movimentacaoBanco);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoSummaryDTO> buscarPorTipo(String tipo) {
        return this.movimentacaoRepository.findByTipo(tipo)
                .stream()
                .map(this.movimentacaoMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoSummaryDTO> buscarPorQuantidadeMaiorIgual(Integer quantidade) {
        return this.movimentacaoRepository.getAllByQuantidadeMaiorIgual(quantidade)
                .stream()
                .map(this.movimentacaoMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoSummaryDTO> buscarPorTipoLimit(String tipo, int limit) {
        return this.movimentacaoRepository.getAllByTipoLimit(tipo, limit)
                .stream()
                .map(this.movimentacaoMapper::entityToSummaryDTO)
                .toList();
    }

    private void aplicarMovimentacao(Produto produto, String tipo, Integer quantidade) {
        validarTipoEQuantidade(tipo, quantidade);

        if (tipo.equalsIgnoreCase("ENTRADA")) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        } else if (tipo.equalsIgnoreCase("SAIDA")) {
            if (produto.getQuantidadeEstoque() < quantidade) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de estoque insuficiente");
            }
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        }
    }

    private void reverterMovimentacao(Produto produto, String tipo, Integer quantidade) {
        validarTipoEQuantidade(tipo, quantidade);

        if (tipo.equalsIgnoreCase("ENTRADA")) {
            if (produto.getQuantidadeEstoque() < quantidade) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nao e possivel reverter a entrada");
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        } else if (tipo.equalsIgnoreCase("SAIDA")) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        }
    }

    private void validarTipoEQuantidade(String tipo, Integer quantidade) {
        if (tipo == null || (!tipo.equalsIgnoreCase("ENTRADA") && !tipo.equalsIgnoreCase("SAIDA"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de movimentacao invalido.");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade invalida.");
        }
    }

    private Movimentacao buscarEntidade(Long id) {
        return this.movimentacaoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimentacao nao encontrada."));
    }

    private Produto buscarProduto(Movimentacao movimentacao) {
        if (movimentacao.getProduto() == null || movimentacao.getProduto().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto nao informado");
        }

        return buscarProdutoPorId(movimentacao.getProduto().getId());
    }

    private Produto buscarProdutoPorId(Long id) {
        return this.produtoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
    }

    private Usuario buscarUsuario(Movimentacao movimentacao) {
        if (movimentacao.getUsuario() == null || movimentacao.getUsuario().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario nao informado");
        }

        return this.usuarioRepository.findById(movimentacao.getUsuario().getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
    }
}
