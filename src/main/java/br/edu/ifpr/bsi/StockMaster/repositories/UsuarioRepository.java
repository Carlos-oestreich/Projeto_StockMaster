package br.edu.ifpr.bsi.StockMaster.repositories;

import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByNome(String nome);

    List<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.perfil = :perfil")
    List<Usuario> getAllByPerfil(@Param("perfil") String perfil);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_usuario u WHERE u.nome_usuario LIKE %:nome% LIMIT :limit")
    List<Usuario> getAllByNomeLikeLimit(@Param("nome") String nome, @Param("limit") int limit);

}
