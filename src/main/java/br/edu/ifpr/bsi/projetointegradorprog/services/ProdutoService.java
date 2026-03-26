package br.edu.ifpr.bsi.projetointegradorprog.services;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.model.produto.Produto;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.FornecedorRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Produto salvar(Produto produto){
        Categoria categoria = categoriaRepository.findById(produto.getCategoria().getId()).orElse(null);
        Fornecedor fornecedor =  fornecedorRepository.findById(produto.getFornecedor().getId()).orElse(null);

        if(categoria == null){
            throw new RuntimeException("Categoria não encontrada.");
        }

        if(fornecedor == null){
            throw new RuntimeException("Fornecedor não encontrado.");
        }

        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        if (produto.getDataCadastro() == null){
            produto.setDataCadastro(LocalDateTime.now());
        }

        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos(){
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id){
        return produtoRepository.findById(id).orElse(null);
    }

    public Produto atualizar(Long id, Produto produto){
        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if(produtoBanco == null){
            return null;
        }

        Categoria categoria = categoriaRepository.findById(produto.getCategoria().getId()).orElse(null);
        Fornecedor fornecedor =   fornecedorRepository.findById(produto.getFornecedor().getId()).orElse(null);

        if(categoria == null){
            throw new RuntimeException("Categoria não encontrada.");
        }

        if(fornecedor == null){
            throw new RuntimeException("Fornecedor não encontrado.");
        }

        produtoBanco.setSku(produto.getSku());
        produtoBanco.setNome(produto.getNome());
        produtoBanco.setDescricao(produto.getDescricao());
        produtoBanco.setMarca(produto.getMarca());
        produtoBanco.setPreco(produto.getPreco());
        produtoBanco.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        produtoBanco.setQuantidadeMinima(produto.getQuantidadeMinima());

        if (produtoBanco.getDataCadastro() != null){
            produtoBanco.setDataCadastro(LocalDateTime.now());
        }

        produtoBanco.setCategoria(categoria);
        produtoBanco.setFornecedor(fornecedor);

        return produtoRepository.save(produtoBanco);
    }

    public boolean deletar(Long id){
        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if(produtoBanco == null){
            return false;
        }

        produtoRepository.delete(produtoBanco);
        return true;
    }

    public List<Produto> buscarPorNome(String nome ){
        return produtoRepository.findByNome(nome);
    }

    public List<Produto> buscarPorSku(String sku){
        return produtoRepository.findBySku(sku);
    }

    public List<Produto> buscarProdutosComEstoqueBaixo(){
        return produtoRepository.getAllProdutosEstoqueBaixo();
    }

    public List<Produto> buscarPorNomeLikeLimit(String nome, int limit){
        return produtoRepository.getAllByNomeLikeLimit(nome, limit);
    }

}
