package br.edu.ifpr.bsi.StockMaster.repositories;

import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByEmpresaId(Long empresaId);

    @Query("SELECT u FROM Usuario u WHERE u.perfil = :perfil AND u.empresa.id = :empresaId")
    List<Usuario> getAllByPerfilAndEmpresaId(@Param("perfil") String perfil, @Param("empresaId") Long empresaId);
}