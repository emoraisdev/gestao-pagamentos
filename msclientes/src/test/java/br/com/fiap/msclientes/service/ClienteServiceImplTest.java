package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.model.Cliente;
import br.com.fiap.msclientes.model.dto.ClienteDTO;
import br.com.fiap.msclientes.model.dto.ClienteIncluirResponseDTO;
import br.com.fiap.msclientes.model.dto.ClienteResponseDTO;
import br.com.fiap.msclientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ClienteServiceImplTest {

    @InjectMocks
    private ClienteServiceImpl service;

    @Mock
    private ClienteRepository repo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIncluir_Sucesso() {
        ClienteDTO dto = new ClienteDTO(
                "12345678900",
                "Nome",
                "email@example.com",
                "123456789",
                "Rua",
                "Cidade",
                "Estado",
                "12345-678",
                "País"
        );

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setCpf(dto.cpf());
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setRua(dto.rua());
        cliente.setCidade(dto.cidade());
        cliente.setEstado(dto.estado());
        cliente.setCep(dto.cep());
        cliente.setPais(dto.pais());

        when(repo.getClienteByCpf(dto.cpf())).thenReturn(Optional.empty());
        when(repo.save(any(Cliente.class))).thenReturn(cliente);

        ClienteIncluirResponseDTO response = service.incluir(dto);

        assertEquals(1L, response.idCliente());
        verify(repo).save(any(Cliente.class));
    }

    @Test
    public void testGetClienteByCpf_Sucesso() {
        String cpf = "12345678900";
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setCpf(cpf);
        cliente.setNome("Nome");
        cliente.setEmail("email@example.com");
        cliente.setTelefone("123456789");
        cliente.setRua("Rua");
        cliente.setCidade("Cidade");
        cliente.setEstado("Estado");
        cliente.setCep("12345-678");
        cliente.setPais("País");

        when(repo.getClienteByCpf(cpf)).thenReturn(Optional.of(cliente));

        ClienteResponseDTO response = service.getClienteByCpf(cpf);

        assertEquals(1L, response.id());
        assertEquals(cpf, response.cpf());
        assertEquals("Nome", response.nome());
        assertEquals("email@example.com", response.email());
        assertEquals("123456789", response.telefone());
        assertEquals("Rua", response.rua());
        assertEquals("Cidade", response.cidade());
        assertEquals("Estado", response.estado());
        assertEquals("12345-678", response.cep());
        assertEquals("País", response.pais());
        verify(repo).getClienteByCpf(cpf);
    }
}