package br.com.fiap.msclientes.exception;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler exceptionHandler;
    private HttpServletRequest requestMock;

    @Before
    public void setUp() {
        exceptionHandler = new ControllerExceptionHandler();
        requestMock = mock(HttpServletRequest.class);
        when(requestMock.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    public void testEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");

        ResponseEntity<StandardError> response = exceptionHandler.entityNotFoundException(exception, requestMock);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertEquals("Entidade Não Encontrada", response.getBody().getError());
        assertEquals("Entidade Entity not found não encontrada.", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    public void testParameterNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Parameter missing");

        ResponseEntity<StandardError> response = exceptionHandler.parameterNotFoundException(exception, requestMock);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Parâmetro Obrigatório", response.getBody().getError());
        assertEquals("Entidade Parameter missing não encontrada.", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    public void testBusinessException() {
        BusinessException exception = new BusinessException("Business error");

        ResponseEntity<StandardError> response = exceptionHandler.businessException(exception, requestMock);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Erro na solicitação", response.getBody().getError());
        assertEquals("Business error", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    public void testMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("Validation error");

        ResponseEntity<StandardError> response = exceptionHandler.methodArgumentNotValidException(exception, requestMock);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Erro de Validação", response.getBody().getError());
        assertEquals("Validation error", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    public void testLoginInvalidoException() {
        LoginInvalido exception = new LoginInvalido();

        ResponseEntity<StandardError> response = exceptionHandler.loginInvalidoException(exception, requestMock);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
        assertEquals("Erro de Autenticação", response.getBody().getError());
        assertEquals("Usuario e/ou Senha Inválidos.", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    public void testTokenInvalidoException() {
        TokenInvalido exception = new TokenInvalido();

        ResponseEntity<StandardError> response = exceptionHandler.tokenInvalidoException(exception, requestMock);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertEquals("Erro de Autenticação", response.getBody().getError());
        assertEquals("Token Inválido.", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }
}