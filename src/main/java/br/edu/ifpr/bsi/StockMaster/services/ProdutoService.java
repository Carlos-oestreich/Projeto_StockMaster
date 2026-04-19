package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.mappers.ProdutoMapper;
import br.edu.ifpr.bsi.StockMaster.model.categoria.Categoria;
import br.edu.ifpr.bsi.StockMaster.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.StockMaster.model.produto.Produto;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.produto.ProdutoSummaryDTO;
import br.edu.ifpr.bsi.StockMaster.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.FornecedorRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoDetailDTO salvar(ProdutoRequestDTO request) {
        validarSkuUnico(request.sku(), null);

        Produto produto = this.produtoMapper.requestDTOToEntity(request);
        Categoria categoria = buscarCategoria(produto);
        Fornecedor fornecedor = buscarFornecedor(produto);

        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        if (produto.getDataCadastro() == null) {
            produto.setDataCadastro(LocalDateTime.now());
        }

        return this.produtoMapper.entityToDetailDTO(this.produtoRepository.save(produto));
    }

    @Transactional(readOnly = true)
    public List<ProdutoSummaryDTO> listarTodos() {
        return this.produtoRepository.findAll()
                .stream()
                .map(this.produtoMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoDetailDTO buscarPorId(Long id) {
        return this.produtoMapper.entityToDetailDTO(buscarEntidade(id));
    }

    @Transactional
    public ProdutoDetailDTO atualizar(Long id, ProdutoRequestDTO request) {
        validarSkuUnico(request.sku(), id);

        Produto produtoBanco = buscarEntidade(id);
        Produto produto = this.produtoMapper.requestDTOToEntity(request);
        Categoria categoria = buscarCategoria(produto);
        Fornecedor fornecedor = buscarFornecedor(produto);

        produtoBanco.setSku(produto.getSku());
        produtoBanco.setNome(produto.getNome());
        produtoBanco.setDescricao(produto.getDescricao());
        produtoBanco.setMarca(produto.getMarca());
        produtoBanco.setPreco(produto.getPreco());
        produtoBanco.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        produtoBanco.setQuantidadeMinima(produto.getQuantidadeMinima());
        produtoBanco.setCategoria(categoria);
        produtoBanco.setFornecedor(fornecedor);

        return this.produtoMapper.entityToDetailDTO(this.produtoRepository.save(produtoBanco));
    }

    @Transactional
    public void deletar(Long id) {
        Produto produtoBanco = buscarEntidade(id);
        this.produtoRepository.delete(produtoBanco);
    }

    @Transactional(readOnly = true)
    public List<ProdutoSummaryDTO> buscarPorNome(String nome) {
        return this.produtoRepository.findByNome(nome)
                .stream()
                .map(this.produtoMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoSummaryDTO buscarPorSku(String sku) {
        return this.produtoRepository.findBySku(sku)
                .map(this.produtoMapper::entityToSummaryDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com este SKU nao encontrado."));

    }

    @Transactional(readOnly = true)
    public List<ProdutoSummaryDTO> buscarProdutosComEstoqueBaixo() {
        return this.produtoRepository.getAllProdutosEstoqueBaixo()
                .stream()
                .map(this.produtoMapper::entityToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoSummaryDTO> buscarPorNomeLikeLimit(String nome, int limit) {
        return this.produtoRepository.getAllByNomeLikeLimit(nome, limit)
                .stream()
                .map(this.produtoMapper::entityToSummaryDTO)
                .toList();
    }

    private Produto buscarEntidade(Long id) {
        return this.produtoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado."));
    }

    private Categoria buscarCategoria(Produto produto) {
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria nao informada.");
        }

        return this.categoriaRepository.findById(produto.getCategoria().getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria nao encontrada."));
    }

    private Fornecedor buscarFornecedor(Produto produto) {
        if (produto.getFornecedor() == null || produto.getFornecedor().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fornecedor nao informado.");
        }

        return this.fornecedorRepository.findById(produto.getFornecedor().getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor nao encontrado."));
    }
    private void validarSkuUnico(String sku, Long id) {
        this.produtoRepository.findBySku(sku).ifPresent(produto -> {
            if (id == null || !produto.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ja existe um produto cadastrado com este SKU.");
            }
        });
    }
}
