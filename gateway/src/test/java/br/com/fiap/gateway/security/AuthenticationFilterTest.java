package br.com.fiap.gateway.security;

import br.com.fiap.gateway.exception.StandardError;
import br.com.fiap.gateway.integration.ClientesServiceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    private AuthenticationFilter authenticationFilter;
    private ClientesServiceApi clientesServiceApi;

    @BeforeEach
    public void setUp() {
        clientesServiceApi = mock(ClientesServiceApi.class);
        authenticationFilter = new AuthenticationFilter(clientesServiceApi);
    }

    @Test
    void deveRetornarErroQuandoTokenNaoForInformado() {
        ServerHttpRequest request = MockServerHttpRequest.get("/").build();
        MockServerWebExchange exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

        GatewayFilter filter = authenticationFilter.apply(new CustomFilterConfig("ADMIN"));

        filter.filter(exchange, exchange1 -> {
            fail("O filtro não deveria continuar sem um token");
            return null;
        }).subscribe();

        assertEquals(HttpStatus.BAD_REQUEST, exchange.getResponse().getStatusCode());
    }

    @Test
    void deveRetornarErroQuandoTokenForInvalido() {
        ServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer tokenInvalido")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

        when(clientesServiceApi.validarToken("tokenInvalido")).thenReturn(null);

        GatewayFilter filter = authenticationFilter.apply(new CustomFilterConfig("ADMIN"));

        filter.filter(exchange, exchange1 -> {
            fail("O filtro não deveria continuar com um token inválido");
            return null;
        }).subscribe();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void deveRetornarStandardErrorQuandoJsonForValido() {
        // Dados de entrada
        String responseBody = "{\"error\": \"Mensagem de erro\", \"status\": 400, \"message\": \"Mensagem de erro\"}";

        // Executa o método a ser testado
        StandardError result = AuthenticationFilter.getStandardError(responseBody);

        // Verifica o resultado
        assertNotNull(result, "O resultado não deve ser nulo");
        assertEquals("Mensagem de erro", result.getMessage(), "A mensagem de erro está incorreta");
        assertEquals(400, result.getStatus(), "O status está incorreto");
    }

    @Test
    void deveRetornarNullQuandoJsonForInvalido() {
        // Dados de entrada
        String responseBody = "dados invalidos";

        // Executa o método a ser testado
        StandardError result = AuthenticationFilter.getStandardError(responseBody);

        // Verifica o resultado
        assertNull(result, "O resultado deve ser nulo quando o JSON for inválido");
    }

    @Test
    void deveRetornarErroDesconhecidoQuandoOcorrerExcecao() {
        ServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer tokenValido")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from((MockServerHttpRequest) request);

        when(clientesServiceApi.validarToken("tokenValido")).thenThrow(new RuntimeException("Erro desconhecido"));

        GatewayFilter filter = authenticationFilter.apply(new CustomFilterConfig("ADMIN"));

        filter.filter(exchange, exchange1 -> {
            fail("O filtro não deveria continuar quando ocorre uma exceção");
            return null;
        }).subscribe();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getResponse().getStatusCode());
    }
}
