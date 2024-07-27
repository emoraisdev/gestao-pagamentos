package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.model.Cliente;
import br.com.fiap.msclientes.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface ClienteService {

    ClienteIncluirResponseDTO incluir(ClienteDTO dto);

    ClienteResponseDTO getClienteByEmail(String email);

    Optional<Cliente> getUsuarioEntidadeByEmail(String email);

    Page<ClienteResponseDTO> listar(PageRequest pageRequest);

}
