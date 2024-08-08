package br.com.fiap.mspagamentos.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import br.com.fiap.mspagamentos.controller.dto.RegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponsePagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponseRegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.service.PagamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PagamentosControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PagamentoService service;

    @InjectMocks
    private PagamentosController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testRegistrar() throws Exception {
        var requestDto = new RegistrarPagamentoDTO(
                "123.456.789-09",
                "1234 5678 1234 5678",
                "12/24",
                "123",
                new BigDecimal("100.00")
        );

        var responseDto = new ResponseRegistrarPagamentoDTO(1L);

        when(service.registrar(any(RegistrarPagamentoDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        verify(service, times(1)).registrar(any(RegistrarPagamentoDTO.class));
    }


    @Test
    public void testConsultarPorCliente() throws Exception {
        String cpf = "12345678900";
        List<ResponsePagamentoDTO> responseDtoList = Collections.emptyList();

        when(service.consultarPorCliente(cpf)).thenReturn(responseDtoList);

        mockMvc.perform(get("/api/pagamentos/cliente/{cpf}", cpf)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDtoList)))
                .andDo(print());

        verify(service, times(1)).consultarPorCliente(cpf);
    }
}