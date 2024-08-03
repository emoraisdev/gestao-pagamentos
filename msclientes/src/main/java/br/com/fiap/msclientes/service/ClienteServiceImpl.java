package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.exception.BusinessException;
import br.com.fiap.msclientes.exception.EntityNotFoundException;
import br.com.fiap.msclientes.model.Cliente;
import br.com.fiap.msclientes.model.dto.*;
import br.com.fiap.msclientes.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository repo;

    @Override
    public ClienteIncluirResponseDTO incluir(ClienteDTO dto) {

        if (repo.getClienteByCpf(dto.cpf()).isPresent()) {
            throw new BusinessException("CPF já utilizado.");
        }

        Cliente cliente = toEntity(dto);

        var clienteSalvo = repo.save(cliente);
        return new ClienteIncluirResponseDTO(clienteSalvo.getId());
    }

    @Override
    public  ClienteResponseDTO getClienteByCpf(String cpf){
        var cliente = repo.getClienteByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException(Cliente.class.getSimpleName()));

        return toClienteResponseDTO(cliente);
    }

    @Override
    public Page<ClienteResponseDTO> listar(PageRequest pageRequest) {
        var clientes =  repo.findAll(pageRequest);
        return clientes.map(this::toClienteResponseDTO);
    }

    private Cliente toEntity(ClienteDTO dto){
        return Cliente.builder()
                .cpf(dto.cpf())
                .nome(dto.nome())
                .email(dto.email())
                .telefone(dto.telefone())
                .rua(dto.rua())
                .cidade(dto.cidade())
                .estado(dto.estado())
                .cep(dto.cep())
                .pais(dto.pais())
                .build();
    }

    private ClienteResponseDTO toClienteResponseDTO(Cliente entidade){
        return new ClienteResponseDTO(entidade.getId(),
                entidade.getCpf(),
                entidade.getNome(),
                entidade.getEmail(),
                entidade.getTelefone(),
                entidade.getRua(),
                entidade.getCidade(),
                entidade.getEstado(),
                entidade.getCep(),
                entidade.getPais());
    }
}
