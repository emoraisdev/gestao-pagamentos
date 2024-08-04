package br.com.fiap.mscartoes.controller.test;

import br.com.fiap.mscartoes.controller.CartaoController;
import br.com.fiap.mscartoes.dto.CartaoDTO;
import br.com.fiap.mscartoes.service.CartaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CartaoControllerTest {

    @InjectMocks
    private CartaoController cartaoController;

    @Mock
    private CartaoService cartaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriar() {
        CartaoDTO cartaoDTO = new CartaoDTO();

        ResponseEntity<Void> response = cartaoController.criar(cartaoDTO);

        verify(cartaoService, times(1)).criar(cartaoDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testConsultar() {
        String cpf = "12345678900";
        String cartao = "1234";
        CartaoDTO cartaoDTO = new CartaoDTO();

        when(cartaoService.consultar(cpf, cartao)).thenReturn(cartaoDTO);

        ResponseEntity<CartaoDTO> response = cartaoController.consultar(cpf, cartao);

        verify(cartaoService, times(1)).consultar(cpf, cartao);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(cartaoDTO);
    }
}
