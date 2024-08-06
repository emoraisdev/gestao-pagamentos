package br.com.fiap.mspagamentos.integracao;

import br.com.fiap.mspagamentos.exception.BusinessException;
import br.com.fiap.mspagamentos.integracao.dto.CartaoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CartaoServiceApi {

    @Value("${api.mscartoes.url}")
    private String urlMsCartoes;

    private static final String API_CARTAO_CONSULTAR = "/api/cartao/%s/%s";

    RestTemplate restTemplate;

    public CartaoServiceApi() {
        restTemplate = new RestTemplate();
    }

    public CartaoDTO consultarCartao(String cpf, String numero) {

        // Formata o URL com os parâmetros fornecidos
        String api = urlMsCartoes + API_CARTAO_CONSULTAR;
        String url = String.format(api, cpf, numero);

        try {
            // Faz a requisição GET
            String retorno = restTemplate.getForObject(url, String.class);

            // Mapeia a resposta para um CartaoDTO
            return new ObjectMapper().readValue(retorno, CartaoDTO.class);
        } catch (HttpClientErrorException e) {

            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new BusinessException("Cartão Não Encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
