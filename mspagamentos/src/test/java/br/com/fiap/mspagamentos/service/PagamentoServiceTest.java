package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.controller.dto.RegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponseRegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.enums.MetodoPagamento;
import br.com.fiap.mspagamentos.enums.StatusPagamento;
import br.com.fiap.mspagamentos.exception.BusinessException;
import br.com.fiap.mspagamentos.exception.SaldoInsuficienteException;
import br.com.fiap.mspagamentos.integracao.CartaoServiceApi;
import br.com.fiap.mspagamentos.integracao.dto.CartaoDTO;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.repository.PagamentoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository repo;

    @Mock
    private CartaoServiceApi cartaoApi;

    @InjectMocks
    private PagamentoServiceImpl pagamentoService;

    private RegistrarPagamentoDTO validDto;
    private CartaoDTO cartaoValido;
    private Pagamento pagamentoSalvo;

    @Before
    public void setup() {
        validDto = new RegistrarPagamentoDTO(
                "123.456.789-09",
                "1234 5678 1234 5678",
                "12/24",
                "123",
                new BigDecimal("100.00")
        );

        cartaoValido = new CartaoDTO();
        cartaoValido.setCpf("123.456.789-09");
        cartaoValido.setNumero("1234 5678 1234 5678");
        cartaoValido.setDataValidade("12/24");
        cartaoValido.setCvv("123");
        cartaoValido.setLimite(new BigDecimal("500.00"));

        pagamentoSalvo = new Pagamento();
        pagamentoSalvo.setId(1L);
        pagamentoSalvo.setCpf(validDto.cpf());
        pagamentoSalvo.setNumeroCartao(validDto.numero());
        pagamentoSalvo.setValor(validDto.valor());
        pagamentoSalvo.setMetodoPagamento(MetodoPagamento.CREDITO);
        pagamentoSalvo.setStatus(StatusPagamento.APROVADO);
    }

    @Test
    public void testRegistrarPagamento_Sucesso() {
        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(cartaoValido);
        when(repo.getSumValorByCpfAndNumero(validDto.cpf(), validDto.numero())).thenReturn(Optional.of(new BigDecimal("100.00")));
        when(repo.save(any(Pagamento.class))).thenReturn(pagamentoSalvo);

        ResponseRegistrarPagamentoDTO response = pagamentoService.registrar(validDto);

        assertNotNull(response);
        verify(repo, times(1)).save(any(Pagamento.class));
    }

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_CartaoNaoEncontrado() {
        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(null);

        pagamentoService.registrar(validDto);
    }

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_DataValidadeInvalida() {
        cartaoValido.setDataValidade("11/24");
        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(cartaoValido);

        pagamentoService.registrar(validDto);
    }

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_CVVInvalido() {
        cartaoValido.setCvv("321");
        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(cartaoValido);

        pagamentoService.registrar(validDto);
    }

    @Test(expected = SaldoInsuficienteException.class)
    public void testRegistrarPagamento_SaldoInsuficiente() {
        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(cartaoValido);
        when(repo.getSumValorByCpfAndNumero(validDto.cpf(), validDto.numero())).thenReturn(Optional.of(new BigDecimal("450.00")));

        pagamentoService.registrar(validDto);
    }
}