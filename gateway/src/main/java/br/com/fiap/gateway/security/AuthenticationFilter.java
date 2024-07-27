package br.com.fiap.gateway.security;

import br.com.fiap.gateway.exception.BusinessException;
import br.com.fiap.gateway.exception.RequestException;
import br.com.fiap.gateway.exception.StandardError;
import br.com.fiap.gateway.integration.ClientesServiceApi;
import br.com.fiap.gateway.integration.dto.ClienteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<CustomFilterConfig> {

    private final ClientesServiceApi clientesServiceApi;

    public AuthenticationFilter(ClientesServiceApi clientesServiceApi) {
        super(CustomFilterConfig.class);
        this.clientesServiceApi = clientesServiceApi;
    }

    @Override
    public GatewayFilter apply(CustomFilterConfig config) {
        return (exchange, chain) -> {

            var token = this.recoverToken(exchange.getRequest());
            if (token != null) {

                try {
                    var cliente = clientesServiceApi.validarToken(token);

                    if (cliente != null) {

                        // Adiciona o Id do cliente para passar na requisição para os microserviços.
                        var request = exchange.getRequest()
                                .mutate()
                                .header("clienteId", cliente.id().toString())
                                .build();

                        return chain.filter(exchange.mutate().request(request).build());
                    } else {
                        return onError(exchange, "Token Inválido.", HttpStatus.UNAUTHORIZED);
                    }

                } catch (RequestException e){
                    StandardError standardError = getStandardError(e.getBody());

                    if (standardError != null) {
                        return onError(exchange, standardError.getMessage(), HttpStatus.valueOf(standardError.getStatus()));
                    } else {
                        return getUnknownErrorValidation(exchange, e);
                    }


                } catch (Exception e){
                    return getUnknownErrorValidation(exchange, e);
                }

            } else {
                return onError(exchange, "Token Não informado", HttpStatus.BAD_REQUEST);
            }
        };
    }

    private static StandardError getStandardError(String responseBody) {
        StandardError standardError = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            standardError = objectMapper.readValue(responseBody, StandardError.class);
        } catch (Exception error) {
            error.printStackTrace();
        }
        return standardError;
    }

    private Mono<Void> getUnknownErrorValidation(ServerWebExchange exchange, Exception e) {
        e.printStackTrace();
        return onError(exchange, "Ocorreu um erro desconhecido na solicitação",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String errorBody = "{\"error\": \"" + errorMessage + "\"}";
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(errorBody.getBytes())));
    }

    private String recoverToken(ServerHttpRequest request) {
        var authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}