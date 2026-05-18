package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.model.auth.AuthLoginDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.auth.AuthLoginRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthLoginDetailDTO login(AuthLoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas"));

        String senhaBanco = usuario.getSenha();
        boolean ok;

        if (senhaBanco != null && senhaBanco.startsWith("$2")) {
            ok = passwordEncoder.matches(request.senha(), senhaBanco);
        } else {
            ok = senhaBanco != null && senhaBanco.equals(request.senha());
            if (ok) {
                usuario.setSenha(passwordEncoder.encode(request.senha()));
                usuarioRepository.save(usuario);
            }
        }

        if (!ok || Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
        }

        String token = "token-" + usuario.getId() + "-" + UUID.randomUUID();

        return new AuthLoginDetailDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getCpf(),
                usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null,
                token
        );
    }
}