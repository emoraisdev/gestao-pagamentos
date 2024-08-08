package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.exception.BusinessException;
import br.com.fiap.msclientes.exception.EntityNotFoundException;
import br.com.fiap.msclientes.model.Cliente;
import br.com.fiap.msclientes.model.dto.ClienteDTO;
import br.com.fiap.msclientes.model.dto.ClienteIncluirResponseDTO;
import br.com.fiap.msclientes.model.dto.ClienteResponseDTO;
import br.com.fiap.msclientes.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void incluir_DeveCriarClienteQuandoDadosValidos() {
        ClienteDTO dto = getClienteDTO();
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.getClienteByCpf(dto.cpf())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteIncluirResponseDTO response = clienteService.incluir(dto);

        assertThat(response).isNotNull();
        verify(clienteRepository).getClienteByCpf(dto.cpf());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void incluir_DeveLancarExceptionQuandoCpfJaExistir() {
        ClienteDTO dto = getClienteDTO();
        when(clienteRepository.getClienteByCpf(dto.cpf())).thenReturn(Optional.of(new Cliente()));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> clienteService.incluir(dto))
                .withMessage("CPF já utilizado.");
    }

    @Test
    void getClienteByCpf_DeveRetornarClienteQuandoExistir() {
        Cliente cliente = new Cliente();
        when(clienteRepository.getClienteByCpf("12345678900")).thenReturn(Optional.of(cliente));

        ClienteResponseDTO response = clienteService.getClienteByCpf("12345678900");

        assertThat(response).isNotNull();
        verify(clienteRepository).getClienteByCpf("12345678900");
    }

    @Test
    void getClienteByCpf_DeveLancarExceptionQuandoNaoExistir() {
        when(clienteRepository.getClienteByCpf("12345678900")).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> clienteService.getClienteByCpf("12345678900"))
                .withMessage("Entidade Cliente não encontrada.");
    }

    @Test
    void listar_DeveRetornarListaDeClientes() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        Page<Cliente> page = new PageImpl<>(Arrays.asList(cliente1, cliente2));
        when(clienteRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<ClienteResponseDTO> response = clienteService.listar(PageRequest.of(0, 10));

        assertThat(response).hasSize(2);
        verify(clienteRepository).findAll(any(PageRequest.class));
    }

    @Test
    void listar_DeveRetornarPaginaVaziaQuandoNaoHouverClientes() {
        Page<Cliente> page = new PageImpl<>(Arrays.asList());
        when(clienteRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<ClienteResponseDTO> response = clienteService.listar(PageRequest.of(0, 10));

        assertThat(response).isEmpty();
        verify(clienteRepository).findAll(any(PageRequest.class));
    }

    @Test
    void getClienteByCpf_DeveRetornarClienteCorreto() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.getClienteByCpf("12345678900")).thenReturn(Optional.of(cliente));

        ClienteResponseDTO response = clienteService.getClienteByCpf("12345678900");

        assertThat(response.id()).isEqualTo(1L);
        verify(clienteRepository).getClienteByCpf("12345678900");
    }

    @Test
    void incluir_DeveChamarSaveUmaVez() {
        ClienteDTO dto = getClienteDTO();
        when(clienteRepository.getClienteByCpf(dto.cpf())).thenReturn(Optional.empty());

        var cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        clienteService.incluir(dto);

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    private static ClienteDTO getClienteDTO() {
        return new ClienteDTO("12345678900",
                "Nome",
                "email@example.com",
                "123456789",
                "Rua",
                "Cidade",
                "Estado",
                "12345-678",
                "Pais");
    }

    @Test
    void incluir_DeveNaoChamarSaveQuandoCpfJaExistir() {
        ClienteDTO dto = getClienteDTO();
        when(clienteRepository.getClienteByCpf(dto.cpf())).thenReturn(Optional.of(new Cliente()));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> clienteService.incluir(dto))
                .withMessage("CPF já utilizado.");

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void incluir_DevePreencherCamposCorretamente() {
        ClienteDTO dto = getClienteDTO();
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.getClienteByCpf(dto.cpf())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        clienteService.incluir(dto);

        verify(clienteRepository).save(argThat(clienteArg ->
                clienteArg.getCpf().equals(dto.cpf()) &&
                        clienteArg.getNome().equals(dto.nome()) &&
                        clienteArg.getEmail().equals(dto.email()) &&
                        clienteArg.getTelefone().equals(dto.telefone()) &&
                        clienteArg.getRua().equals(dto.rua()) &&
                        clienteArg.getCidade().equals(dto.cidade()) &&
                        clienteArg.getEstado().equals(dto.estado()) &&
                        clienteArg.getCep().equals(dto.cep()) &&
                        clienteArg.getPais().equals(dto.pais())
        ));
    }

    @Test
    void listar_DeveChamarFindAllUmaVez() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        Page<Cliente> page = new PageImpl<>(Arrays.asList(cliente1, cliente2));
        when(clienteRepository.findAll(any(PageRequest.class))).thenReturn(page);

        clienteService.listar(PageRequest.of(0, 10));

        verify(clienteRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void getClienteByCpf_DeveChamarRepoUmaVez() {
        when(clienteRepository.getClienteByCpf("12345678900")).thenReturn(Optional.of(new Cliente()));

        clienteService.getClienteByCpf("12345678900");

        verify(clienteRepository, times(1)).getClienteByCpf("12345678900");
    }
}
