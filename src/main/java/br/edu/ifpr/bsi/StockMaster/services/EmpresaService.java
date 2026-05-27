package br.edu.ifpr.bsi.StockMaster.services;

import br.edu.ifpr.bsi.StockMaster.model.empresa.Empresa;
import br.edu.ifpr.bsi.StockMaster.model.empresa.EmpresaDetailDTO;
import br.edu.ifpr.bsi.StockMaster.model.empresa.EmpresaRequestDTO;
import br.edu.ifpr.bsi.StockMaster.model.usuario.Usuario;
import br.edu.ifpr.bsi.StockMaster.repositories.EmpresaRepository;
import br.edu.ifpr.bsi.StockMaster.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private EmpresaDetailDTO toDTO(Empresa empresa) {
        List<Usuario> donos = usuarioRepository.getAllByPerfilAndEmpresaId("DONO", empresa.getId());
        String responsavelNome = donos.isEmpty() ? null : donos.get(0).getNome();
        String responsavelEmail = donos.isEmpty() ? null : donos.get(0).getEmail();

        return new EmpresaDetailDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getEmail(),
                empresa.getTelefone(),
                empresa.getEndereco(),
                empresa.getSuporte(),
                empresa.getLogo(),
                responsavelNome,
                responsavelEmail,
                empresa.getAtivo()
        );
    }

    public EmpresaDetailDTO obter(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));
        return toDTO(empresa);
    }

    @Transactional
    public EmpresaDetailDTO atualizar(Long id, EmpresaRequestDTO request) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        empresa.setNome(request.nome());
        empresa.setCnpj(request.cnpj());
        empresa.setEmail(request.email());
        empresa.setTelefone(request.telefone());
        empresa.setEndereco(request.endereco());
        empresa.setSuporte(request.suporte());
        if (request.logo() != null) empresa.setLogo(request.logo());

        return toDTO(empresaRepository.save(empresa));
    }

    @Transactional
    public EmpresaDetailDTO salvar(EmpresaRequestDTO request) {
        Empresa empresa = new Empresa();
        empresa.setNome(request.nome());
        empresa.setCnpj(request.cnpj());
        empresa.setEmail(request.email());
        empresa.setTelefone(request.telefone());
        empresa.setEndereco(request.endereco());
        empresa.setSuporte(request.suporte());
        empresa.setLogo(request.logo());
        empresa.setAtivo(true);
        return toDTO(empresaRepository.save(empresa));
    }

    @Transactional
    public EmpresaDetailDTO atualizarLogo(Long id, MultipartFile file) throws IOException {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        // Deletar logo antiga se existir
        if (empresa.getLogo() != null) {
            cloudinaryService.deletar(empresa.getLogo());
        }

        String url = cloudinaryService.upload(file);
        empresa.setLogo(url);
        return toDTO(empresaRepository.save(empresa));
    }
}