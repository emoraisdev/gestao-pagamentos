package br.com.fiap.msclientes.controller;

import br.com.fiap.msclientes.model.Usuario;
import br.com.fiap.msclientes.model.dto.LoginDTO;
import br.com.fiap.msclientes.model.dto.TokenDTO;
import br.com.fiap.msclientes.service.AutenticacaoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutenticacaoController.class)
public class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutenticacaoServiceImpl service;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testLogar_Sucesso() throws Exception {
        LoginDTO loginDTO = new LoginDTO("usuario", "senha");
        TokenDTO tokenDTO = new TokenDTO("mockedToken123");

        OngoingStubbing<TokenDTO> tokenDTOOngoingStubbing = when(service.logar(any(LoginDTO.class))).thenReturn(tokenDTO);

        mockMvc.perform(post("/api/autenticacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("mockedToken123")));
    }

    @Test
    public void testValidar_Sucesso() throws Exception {
        TokenDTO tokenDTO = new TokenDTO("mockedToken123");
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsuario("John Doe");

        when(service.validarToken(anyString())).thenReturn(usuario);

        mockMvc.perform(post("/api/autenticacao/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.usuario", is("John Doe")));
    }
}