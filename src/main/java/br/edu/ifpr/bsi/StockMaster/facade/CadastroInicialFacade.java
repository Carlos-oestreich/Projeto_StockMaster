package br.edu.ifpr.bsi.StockMaster.facade;

import br.edu.ifpr.bsi.StockMaster.model.cadastroInicial.CadastroInicialRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.empresa.Empresa;
import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.repositories.EmpresaRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class CadastroInicialFacade {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Map<String, Object> executar(CadastroInicialRequestDTO request) {

        // Valida campos
        if (request.nomeEmpresa() == null || request.nomeEmpresa().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da empresa é obrigatório.");
        if (request.nome() == null || request.nome().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do responsável é obrigatório.");
        if (request.email() == null || request.email().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail é obrigatório.");
        if (request.senha() == null || request.senha().length() < 8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha deve ter no mínimo 8 caracteres.");

        // Verifica e-mail duplicado
        boolean emailExiste = usuarioRepository.findByEmail(request.email()).stream().findFirst().isPresent();
        if (emailExiste)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");

        // Cria empresa
        Empresa empresa = new Empresa();
        empresa.setNome(request.nomeEmpresa());
        empresa.setCnpj(request.cnpjEmpresa());
        empresa.setEmail(request.emailEmpresa());
        empresa.setTelefone(request.telefoneEmpresa());
        empresa.setAtivo(true);
        empresa = empresaRepository.save(empresa);

        // Cria usuário DONO
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setPerfil("DONO");
        usuario.setMatricula(request.matricula() != null ? request.matricula() : "");
        usuario.setAtivo(true);
        usuario.setEmpresa(empresa);
        usuarioRepository.save(usuario);

        return Map.of(
                "message", "Empresa e usuário criados com sucesso.",
                "empresaId", empresa.getId(),
                "empresaNome", empresa.getNome()
        );
    }
}