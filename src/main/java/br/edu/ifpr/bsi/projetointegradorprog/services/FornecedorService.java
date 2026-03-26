package br.edu.ifpr.bsi.projetointegradorprog.services;

import br.edu.ifpr.bsi.projetointegradorprog.model.fornecedor.Fornecedor;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.CategoriaRepository;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Fornecedor salvar(Fornecedor fornecedor){
        return fornecedorRepository.save(fornecedor);
    }

    public List<Fornecedor> listarTodos(){
        return fornecedorRepository.findAll();
    }

    public Fornecedor buscarPorId(Long id){
        return fornecedorRepository.findById(id).orElse(null);
    }

    public Fornecedor atualizar(Long id, Fornecedor fornecedor){
        Fornecedor fornecedorBanco = fornecedorRepository.findById(id).orElse(null);

        if(fornecedorBanco == null){
            return null;
        }

        fornecedorBanco.setNome(fornecedor.getNome());
        fornecedorBanco.setCnpj(fornecedor.getCnpj());
        fornecedorBanco.setEmail(fornecedor.getEmail());
        fornecedorBanco.setTelefone(fornecedor.getTelefone());
        fornecedorBanco.setAtivo(fornecedor.getAtivo());

        return fornecedorRepository.save(fornecedorBanco);
    }

    public boolean deletar(Long id){
        Fornecedor fornecedorBanco = fornecedorRepository.findById(id).orElse(null);

        if(fornecedorBanco == null){
            return false;
        }

        fornecedorRepository.delete(fornecedorBanco);
        return true;
    }

    public List<Fornecedor> buscarPorNome(String nome){
        return fornecedorRepository.findByNome(nome);
    }

    public List<Fornecedor> buscarPorCnpj(String cnpj){
        return fornecedorRepository.findByCnpj(cnpj);
    }

    public List<Fornecedor> buscarPorNomeLike(String nome){
        return fornecedorRepository.getAllByNomeLike(nome);
    }

    public List<Fornecedor> buscarPorAtivoLimit(boolean ativo, int limit){
        return fornecedorRepository.getAllByAtivoLimit(ativo, limit);
    }


}
