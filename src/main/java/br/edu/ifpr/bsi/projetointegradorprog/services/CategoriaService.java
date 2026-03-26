package br.edu.ifpr.bsi.projetointegradorprog.services;

import br.edu.ifpr.bsi.projetointegradorprog.model.categoria.Categoria;
import br.edu.ifpr.bsi.projetointegradorprog.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria salvar(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarTodos(){
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id){
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria atualizar(Long id, Categoria categoria){
        Categoria categoriaBanco = categoriaRepository.findById(id).orElse(null);

        if (categoriaBanco == null){
            return null;
        }

        categoriaBanco.setNome(categoria.getNome());
        categoriaBanco.setDescricao(categoria.getDescricao());
        categoriaBanco.setSetor(categoria.getSetor());
        categoriaBanco.setCodigoInterno(categoria.getCodigoInterno());
        categoriaBanco.setAtivo(categoria.getAtivo());

        return categoriaRepository.save(categoriaBanco);
    }

    public boolean deletar(Long id){
        Categoria categoriaBanco = categoriaRepository.findById(id).orElse(null);

        if (categoriaBanco == null){
            return false;
        }

        categoriaRepository.delete(categoriaBanco);
        return true;
    }

    public List<Categoria> buscarPorNome(String nome){
        return categoriaRepository.findByNome(nome);
    }

    public List<Categoria> buscarPorSetorLike(String setor){
        return categoriaRepository.getAllBySetorLike(setor);
    }

    public List<Categoria> buscarPorNomeLikeLimit(String nome, int limit){
        return categoriaRepository.getAllByNomeLikeLimit(nome, limit);
    }
}
