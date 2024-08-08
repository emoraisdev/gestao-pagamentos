package br.com.fiap.mspagamentos.exception;

import br.com.fiap.mspagamentos.controller.PagamentosController;
import br.com.fiap.mspagamentos.service.PagamentoService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Parameter;
import java.util.Collections;
import org.springframework.validation.FieldError;

@WebMvcTest
public class ControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagamentoService service;

    @Test
    void testMethodArgumentNotValidExceptionHandling() throws Exception {
        mockMvc.perform(post("/api/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\": \"\", \"numero\": \"\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Erro de Validação"))
                .andExpect(jsonPath("$.path").value("/api/pagamentos"));
    }

    @Test
    void testBusinessExceptionHandling() throws Exception {
        when(service.consultarPorCliente(any(String.class))).thenThrow(new BusinessException("Saldo insuficiente"));

        mockMvc.perform(get("/api/pagamentos/cliente/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Erro na solicitação"))
                .andExpect(jsonPath("$.message").value("Saldo insuficiente"))
                .andExpect(jsonPath("$.path").value("/api/pagamentos/cliente/123"));
    }

    @Test
    void testSaldoInsuficienteExceptionHandling() throws Exception {
        when(service.consultarPorCliente(any(String.class))).thenThrow(new SaldoInsuficienteException("Saldo insuficiente"));

        mockMvc.perform(get("/api/pagamentos/cliente/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPaymentRequired())
                .andExpect(jsonPath("$.error").value("Erro na solicitação"))
                .andExpect(jsonPath("$.message").value("Saldo insuficiente"))
                .andExpect(jsonPath("$.path").value("/api/pagamentos/cliente/123"));
    }
}
