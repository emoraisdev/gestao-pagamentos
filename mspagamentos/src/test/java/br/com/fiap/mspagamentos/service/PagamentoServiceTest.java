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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_DataValidadeExpirada() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        String expiredDate = oneMonthAgo.format(DateTimeFormatter.ofPattern("M/yy"));
        cartaoValido.setDataValidade(expiredDate);
        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(cartaoValido);

        pagamentoService.registrar(validDto);
    }

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_CartaoVencido() {

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        String expiredDate = oneMonthAgo.format(DateTimeFormatter.ofPattern("M/yy"));

        RegistrarPagamentoDTO dto = new RegistrarPagamentoDTO(
                validDto.cpf(),
                validDto.numero(),
                expiredDate,
                validDto.cvv(),
                new BigDecimal(10)
        );

        cartaoValido.setDataValidade(expiredDate);
        when(cartaoApi.consultarCartao(dto.cpf(), dto.numero())).thenReturn(cartaoValido);

        pagamentoService.registrar(dto);
    }

    @Test
    public void testRegistrarPagamento_SaldoExato() {
        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(cartaoValido);
        when(repo.getSumValorByCpfAndNumero(validDto.cpf(), validDto.numero())).thenReturn(Optional.of(new BigDecimal("400.00")));
        when(repo.save(any(Pagamento.class))).thenReturn(pagamentoSalvo);

        ResponseRegistrarPagamentoDTO response = pagamentoService.registrar(validDto);

        assertNotNull(response);
        verify(repo, times(1)).save(any(Pagamento.class));
    }

    @Test
    public void testRegistrarPagamento_ValorDez() {
        RegistrarPagamentoDTO dezValueDto = new RegistrarPagamentoDTO(
                "123.456.789-09",
                "1234 5678 1234 5678",
                "12/24",
                "123",
                new BigDecimal(10)
        );

        when(cartaoApi.consultarCartao(dezValueDto.cpf(), dezValueDto.numero())).thenReturn(cartaoValido);
        when(repo.getSumValorByCpfAndNumero(dezValueDto.cpf(), dezValueDto.numero())).thenReturn(Optional.of(new BigDecimal("100.00")));
        when(repo.save(any(Pagamento.class))).thenReturn(pagamentoSalvo);

        ResponseRegistrarPagamentoDTO response = pagamentoService.registrar(dezValueDto);

        assertNotNull(response);
        verify(repo, times(1)).save(any(Pagamento.class));
    }

    @Test
    public void testRegistrarPagamento_LimiteExato() {
        RegistrarPagamentoDTO exactLimitDto = new RegistrarPagamentoDTO(
                "123.456.789-09",
                "1234 5678 1234 5678",
                "12/24",
                "123",
                new BigDecimal("500.00")
        );

        when(cartaoApi.consultarCartao(exactLimitDto.cpf(), exactLimitDto.numero())).thenReturn(cartaoValido);
        when(repo.getSumValorByCpfAndNumero(exactLimitDto.cpf(), exactLimitDto.numero())).thenReturn(Optional.of(new BigDecimal("0.00")));
        when(repo.save(any(Pagamento.class))).thenReturn(pagamentoSalvo);

        ResponseRegistrarPagamentoDTO response = pagamentoService.registrar(exactLimitDto);

        assertNotNull(response);
        verify(repo, times(1)).save(any(Pagamento.class));
    }

    @Test(expected = SaldoInsuficienteException.class)
    public void testRegistrarPagamento_LimiteExcedido() {
        RegistrarPagamentoDTO overLimitDto = new RegistrarPagamentoDTO(
                "123.456.789-09",
                "1234 5678 1234 5678",
                "12/24",
                "123",
                new BigDecimal("600.00")
        );

        when(cartaoApi.consultarCartao(overLimitDto.cpf(), overLimitDto.numero())).thenReturn(cartaoValido);
        when(repo.getSumValorByCpfAndNumero(overLimitDto.cpf(), overLimitDto.numero())).thenReturn(Optional.of(new BigDecimal("0.00")));

        pagamentoService.registrar(overLimitDto);
    }

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_DadosCartaoIncompletos() {
        CartaoDTO incompleteCartao = new CartaoDTO();
        incompleteCartao.setCpf("123.456.789-09");
        incompleteCartao.setNumero("1234 5678 1234 5678");
        incompleteCartao.setDataValidade(""); // Dados incompletos
        incompleteCartao.setCvv("123");
        incompleteCartao.setLimite(new BigDecimal("500.00"));

        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(incompleteCartao);

        pagamentoService.registrar(validDto);
    }

    // Pagamento com Dados Válidos, mas Cartão com Limite 0
    @Test(expected = SaldoInsuficienteException.class)
    public void testRegistrarPagamento_LimiteZero() {
        CartaoDTO zeroLimitCartao = new CartaoDTO();
        zeroLimitCartao.setCpf("123.456.789-09");
        zeroLimitCartao.setNumero("1234 5678 1234 5678");
        zeroLimitCartao.setDataValidade("12/24");
        zeroLimitCartao.setCvv("123");
        zeroLimitCartao.setLimite(BigDecimal.ZERO);

        when(cartaoApi.consultarCartao(validDto.cpf(), validDto.numero())).thenReturn(zeroLimitCartao);
        when(repo.getSumValorByCpfAndNumero(validDto.cpf(), validDto.numero())).thenReturn(Optional.of(BigDecimal.ZERO));

        pagamentoService.registrar(validDto);
    }

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_CVVNulo() {
        RegistrarPagamentoDTO nullCvvDto = new RegistrarPagamentoDTO(
                "123.456.789-09",
                "1234 5678 1234 5678",
                "12/24",
                null, // CVV nulo
                new BigDecimal("100.00")
        );

        when(cartaoApi.consultarCartao(nullCvvDto.cpf(), nullCvvDto.numero())).thenReturn(cartaoValido);

        pagamentoService.registrar(nullCvvDto);
    }

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_CPFInvalido() {
        RegistrarPagamentoDTO invalidCpfDto = new RegistrarPagamentoDTO(
                "000.000.000-00", // CPF inválido
                "1234 5678 1234 5678",
                "12/24",
                "123",
                new BigDecimal("100.00")
        );

        when(cartaoApi.consultarCartao(invalidCpfDto.cpf(), invalidCpfDto.numero())).thenReturn(null);

        pagamentoService.registrar(invalidCpfDto);
    }

    @Test(expected = BusinessException.class)
    public void testRegistrarPagamento_NumeroCartaoInvalido() {
        RegistrarPagamentoDTO invalidNumeroDto = new RegistrarPagamentoDTO(
                "123.456.789-09",
                "0000 0000 0000 0000", // Número de cartão inválido
                "12/24",
                "123",
                new BigDecimal("100.00")
        );

        when(cartaoApi.consultarCartao(invalidNumeroDto.cpf(), invalidNumeroDto.numero())).thenReturn(null);

        pagamentoService.registrar(invalidNumeroDto);
    }

    @Test
    public void testConsultarPorCliente() {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(new BigDecimal("100.00"));
        pagamento.setDescricao("Compra");
        pagamento.setMetodoPagamento(MetodoPagamento.CREDITO);
        pagamento.setStatus(StatusPagamento.APROVADO);
        when(repo.getPagamentoByCpf(validDto.cpf())).thenReturn(List.of(pagamento));

        var response = pagamentoService.consultarPorCliente(validDto.cpf());

        assertNotNull(response);
        assertNotNull(response.get(0));
        verify(repo, times(1)).getPagamentoByCpf(validDto.cpf());
    }
}