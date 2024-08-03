package br.com.fiap.gateway.integration;

import br.com.fiap.gateway.exception.BusinessException;
import br.com.fiap.gateway.exception.RequestException;
import br.com.fiap.gateway.integration.dto.ClienteDTO;
import br.com.fiap.gateway.integration.dto.TokenDTO;
import br.com.fiap.gateway.integration.dto.UsuarioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ClientesServiceApi {

    @Value("${api.msclientes.server}")
    private String urlMsClientes;

    private static final String API_AUTENTICACAO_VALIDAR = "/api/autenticacao/validar";

    RestTemplate restTemplate;

    public ClientesServiceApi() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

                var statusCode = response.getStatusCode();
                if (statusCode == HttpStatus.BAD_REQUEST) {
                    // Tratamento personalizado para código 400
                    throw RequestException.builder().body(StreamUtils.copyToString(response.getBody(),
                            StandardCharsets.UTF_8)).build();
                } else {
                    // Tratamento padrão para outros códigos de erro
                    super.handleError(response);
                }
            }
        });
    }

    public UsuarioDTO validarToken(String token) {

        var requestBody = new TokenDTO(token);

        var retorno = restTemplate.postForObject(
                urlMsClientes + API_AUTENTICACAO_VALIDAR, // URL
                requestBody, // Corpo da requisição
                String.class // Tipo da resposta
        );

        try {
            return new ObjectMapper().readValue(retorno, UsuarioDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
