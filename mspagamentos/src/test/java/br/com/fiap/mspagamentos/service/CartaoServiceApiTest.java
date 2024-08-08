package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.exception.BusinessException;
import br.com.fiap.mspagamentos.integracao.CartaoServiceApi;
import br.com.fiap.mspagamentos.integracao.dto.CartaoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartaoServiceApiTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CartaoServiceApi cartaoServiceApi;

    @Value("${api.mscartoes.url}")
    private String urlMsCartoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsultarCartao_Success() throws Exception {
        String cpf = "12345678900";
        String numero = "1234567890123456";
        String url = String.format(urlMsCartoes + "/api/cartao/%s/%s", cpf, numero);

        CartaoDTO expectedCartaoDTO = new CartaoDTO();
        expectedCartaoDTO.setCpf(cpf);
        expectedCartaoDTO.setNumero(numero);

        when(restTemplate.getForObject(anyString(), any())).thenReturn(new ObjectMapper().writeValueAsString(expectedCartaoDTO));

        CartaoDTO result = cartaoServiceApi.consultarCartao(cpf, numero);

        assertNotNull(result);
        assertEquals(expectedCartaoDTO, result);
    }

    @Test
    public void testConsultarCartao_NotFound() {
        String cpf = "12345678900";
        String numero = "1234567890123456";
        String url = "http://test-url/api/cartao/" + cpf + "/" + numero;

        when(restTemplate.getForObject(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        BusinessException thrown = assertThrows(BusinessException.class, () -> cartaoServiceApi.consultarCartao(cpf, numero));
        assertEquals("Cartão Não Encontrado.", thrown.getMessage(), "A exceção lançada deve ter a mensagem esperada");
    }
}