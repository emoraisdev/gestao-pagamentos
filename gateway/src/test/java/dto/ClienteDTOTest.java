package dto;

import br.com.fiap.gateway.integration.dto.ClienteDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClienteDTOTest {

    @Test
    public void testClienteDTO() {
        ClienteDTO cliente = new ClienteDTO(
                1L, "12345678901", "Teste", "teste@example.com",
                "11987654321", "Rua", "São Paulo", "SP",
                "01234-567", "Brasil"
        );

        assertEquals(1L, cliente.id());
        assertEquals("12345678901", cliente.cpf());
        assertEquals("Teste", cliente.nome());
        assertEquals("teste@example.com", cliente.email());
        assertEquals("11987654321", cliente.telefone());
        assertEquals("Rua", cliente.rua());
        assertEquals("São Paulo", cliente.cidade());
        assertEquals("SP", cliente.estado());
        assertEquals("01234-567", cliente.cep());
        assertEquals("Brasil", cliente.pais());
    }
}
