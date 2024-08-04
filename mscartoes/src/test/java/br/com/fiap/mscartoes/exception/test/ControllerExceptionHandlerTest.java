package br.com.fiap.mscartoes.exception.test;

import br.com.fiap.mscartoes.dto.CartaoDTO;
import br.com.fiap.mscartoes.exception.BusinessException;
import br.com.fiap.mscartoes.exception.EntityNotFoundException;
import br.com.fiap.mscartoes.exception.LimitCardException;
import br.com.fiap.mscartoes.exception.NotAutorizedException;
import br.com.fiap.mscartoes.service.CartaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
public class ControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartaoService cartaoService;

    private CartaoDTO cartaoDTO;

    @BeforeEach
    public void setup() {
        cartaoDTO = new CartaoDTO("12345678901", 5000L ,"1234 5678 9876 5432",  "12/24", "123");
    }

    @Test
    public void whenBusinessExceptionThrown_thenInternalServerError() throws Exception {
        doThrow(new BusinessException("Erro de negócio"))
                .when(cartaoService).criar(any(CartaoDTO.class));

        mockMvc.perform(post("/api/cartao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\": \"12345678901\", \"numero\": \"1234-5678-9876-5432\", \"limite\": 5000.0, \"data_validade\": \"12/24\", \"cvv\": \"123\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Erro na solicitação"))
                .andExpect(jsonPath("$.message").value("numero do cartao deve estar no formato '0000 0000 0000 0000'"));
    }

    @Test
    public void whenLimitCardExceptionThrown_thenForbidden() throws Exception {
        doThrow(new LimitCardException())
                .when(cartaoService).criar(any(CartaoDTO.class));

        mockMvc.perform(post("/api/cartao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\": \"12345678901\", \"numero\": \"1234 5678 9876 5432\", \"limite\": 5000, \"data_validade\": \"12/24\", \"cvv\": \"123\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Limite de cartões atingido"))
                .andExpect(jsonPath("$.message").value("Limite máximo de cartões por cliente atingido"));
    }

    @Test
    public void whenEntityNotFoundExceptionThrown_thenNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Entidade Não Encontrada"))
                .when(cartaoService).criar(any(CartaoDTO.class));

        mockMvc.perform(post("/api/cartao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\": \"12345678901\", \"numero\": \"1234 5678 9876 5432\", \"limite\": 5000.0, \"data_validade\": \"12/24\", \"cvv\": \"123\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Entidade Não Encontrada"))
                .andExpect(jsonPath("$.message").value("Entidade Não Encontrada não encontrado."));
    }

    @Test
    public void whenNotAutorizedExceptionThrown_thenUnauthorized() throws Exception {
        doThrow(new NotAutorizedException())
                .when(cartaoService).criar(any(CartaoDTO.class));

        mockMvc.perform(post("/api/cartao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\": \"12345678901\", \"numero\": \"1234 5678 9876 5432\", \"limite\": 5000.0, \"data_validade\": \"12/24\", \"cvv\": \"123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Não Autorizado"))
                .andExpect(jsonPath("$.message").value("Cliente não autorizado para essa transação"));
    }

    @Test
    public void whenMethodArgumentNotValidExceptionThrown_thenInternalServerError() throws Exception {
        mockMvc.perform(post("/api/cartao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\": \"\", \"numero\": \"\", \"limite\": 0, \"data_validade\": \"\", \"cvv\": \"\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Erro na solicitação"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}