package br.com.fiap.mscartoes.service.implementation.test;


import br.com.fiap.mscartoes.dto.CartaoDTO;
import br.com.fiap.mscartoes.dto.ClienteResponseDTO;
import br.com.fiap.mscartoes.exception.BusinessException;
import br.com.fiap.mscartoes.exception.EntityNotFoundException;
import br.com.fiap.mscartoes.exception.LimitCardException;
import br.com.fiap.mscartoes.exception.NotAutorizedException;
import br.com.fiap.mscartoes.model.Cartao;
import br.com.fiap.mscartoes.repository.CartaoRepository;
import br.com.fiap.mscartoes.service.implementation.CartaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartaoServiceImplTest {

    @InjectMocks
    private CartaoServiceImpl cartaoService;

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarClienteExistenteCartaoNaoExistenteEQuantidadeOK() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setCpf("12345678900");
        cartaoDTO.setNumero("1234");

        when(restTemplate.getForEntity(anyString(), eq(ClienteResponseDTO.class)))
                .thenReturn(ResponseEntity.ok(new ClienteResponseDTO()));
        when(cartaoRepository.findByCpfAndNumero(cartaoDTO.getCpf(), cartaoDTO.getNumero())).thenReturn(Optional.empty());
        when(cartaoRepository.countByCpf(cartaoDTO.getCpf())).thenReturn(1L);

        cartaoService.criar(cartaoDTO);

        verify(cartaoRepository, times(1)).save(any(Cartao.class));
    }

    @Test
    void testCriarClienteExistenteCartaoExistente() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setCpf("12345678900");
        cartaoDTO.setNumero("1234");

        when(restTemplate.getForEntity(anyString(), eq(ClienteResponseDTO.class)))
                .thenReturn(ResponseEntity.ok(new ClienteResponseDTO()));
        when(cartaoRepository.findByCpfAndNumero(cartaoDTO.getCpf(), cartaoDTO.getNumero())).thenReturn(Optional.of(new Cartao()));

        assertThatThrownBy(() -> cartaoService.criar(cartaoDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("O mesmo numero de cartão já existe para o CPF informado");
    }

    @Test
    void testCriarClienteExistenteQuantidadeExcedida() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setCpf("12345678900");

        when(restTemplate.getForEntity(anyString(), eq(ClienteResponseDTO.class)))
                .thenReturn(ResponseEntity.ok(new ClienteResponseDTO()));
        when(cartaoRepository.findByCpfAndNumero(cartaoDTO.getCpf(), cartaoDTO.getNumero())).thenReturn(Optional.empty());
        when(cartaoRepository.countByCpf(cartaoDTO.getCpf())).thenReturn(2L);

        assertThatThrownBy(() -> cartaoService.criar(cartaoDTO))
                .isInstanceOf(LimitCardException.class);
    }

    @Test
    void testCriarClienteNaoExistente() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setCpf("12345678900");

        when(restTemplate.getForEntity(anyString(), eq(ClienteResponseDTO.class)))
                .thenReturn(new ResponseEntity<ClienteResponseDTO>(HttpStatusCode.valueOf(500)));

        assertThatThrownBy(() -> cartaoService.criar(cartaoDTO))
                .isInstanceOf(NotAutorizedException.class);
    }

    @Test
    void testCriarErroServidorCliente() {
        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setCpf("12345678900");

        when(restTemplate.getForEntity(anyString(), eq(ClienteResponseDTO.class)))
                .thenThrow(new HttpServerErrorException(HttpStatusCode.valueOf(500), "Server error"));

        assertThatThrownBy(() -> cartaoService.criar(cartaoDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Erro no servidor ao buscar Cliente: ");
    }

    @Test
    void testConsultarCartaoExistente() {
        Cartao cartao = new Cartao();
        cartao.setCpf("12345678900");
        cartao.setNumero("1234");

        CartaoDTO expectedDTO = new CartaoDTO();
        expectedDTO.setCpf(cartao.getCpf());
        expectedDTO.setNumero(cartao.getNumero());

        when(cartaoRepository.findByCpfAndNumero(cartao.getCpf(), cartao.getNumero())).thenReturn(Optional.of(cartao));

        CartaoDTO resultDTO = cartaoService.consultar(cartao.getCpf(), cartao.getNumero());

        assertThat(resultDTO).isEqualTo(expectedDTO);
    }

    @Test
    void testConsultarCartaoNaoExistente() {
        when(cartaoRepository.findByCpfAndNumero(anyString(), anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartaoService.consultar("12345678900", "1234"))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
