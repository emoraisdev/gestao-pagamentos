package integration;

import br.com.fiap.gateway.integration.ClientesServiceApi;
import br.com.fiap.gateway.integration.dto.UsuarioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClientesServiceApiTest {

    @InjectMocks
    private ClientesServiceApi clientesServiceApi;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidarTokenSuccess() throws Exception {
        String token = "testToken";
        String responseString = "{\"id\":1,\"usuario\":\"teste\",\"senha\":\"123456\"}";
        UsuarioDTO expectedUsuario = new UsuarioDTO(1L, "teste", "123456");

        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenReturn(responseString);

        when(objectMapper.readValue(responseString, UsuarioDTO.class))
                .thenReturn(expectedUsuario);

        UsuarioDTO result = clientesServiceApi.validarToken(token);

        assertEquals(expectedUsuario, result);
    }


    @Test
    public void testValidarTokenBadRequest() throws Exception {
        String token = "testToken";
        ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);

        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(response.getBody()).thenReturn(new ByteArrayInputStream("Error".getBytes()));

        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            clientesServiceApi.validarToken(token);
        });

        assertEquals("400 BAD_REQUEST", exception.getMessage());
    }
}
