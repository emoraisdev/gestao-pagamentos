package br.com.fiap.msclientes.controller;

import br.com.fiap.msclientes.model.dto.ClienteResponseDTO;
import br.com.fiap.msclientes.repository.ClienteRepository;
import br.com.fiap.msclientes.service.ClienteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteServiceImpl service;

    private ObjectMapper objectMapper;

    @MockBean
    private ClienteRepository repo;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testListar_Sucesso() throws Exception {
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(
                1L,
                "12345678900",
                "Nome",
                "email@example.com",
                "123456789",
                "Rua",
                "Cidade",
                "Estado",
                "12345-678",
                "País"
        );
        Page<ClienteResponseDTO> page = new PageImpl<>(Collections.singletonList(clienteResponseDTO), PageRequest.of(0, 10), 1);

        when(service.listar(PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/api/cliente")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].cpf", is("12345678900")));
    }

    @Test
    public void testVerifyByCpf_Sucesso() throws Exception {
        String cpf = "12345678900";

        when(service.getClienteByCpf(cpf)).thenReturn(new ClienteResponseDTO(
                1L,
                cpf,
                "Nome",
                "email@example.com",
                "123456789",
                "Rua",
                "Cidade",
                "Estado",
                "12345-678",
                "País"
        ));

        mockMvc.perform(get("/api/cliente/exists-by-cpf/{cpf}", cpf)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}