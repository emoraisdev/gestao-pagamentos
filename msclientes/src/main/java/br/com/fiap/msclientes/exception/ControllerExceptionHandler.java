package br.com.fiap.msclientes.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFoundException(final EntityNotFoundException erro, final HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(getStandardError(HttpStatus.NOT_FOUND.value(), "Entidade Não Encontrada", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ParameterNotFoundException.class)
    public ResponseEntity<StandardError> parameterNotFoundException(final EntityNotFoundException erro, final HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(getStandardError(HttpStatus.BAD_REQUEST.value(), "Parâmetro Obrigatório", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> businessException(BusinessException erro, HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(getStandardError(HttpStatus.BAD_REQUEST.value(), "Erro na solicitação", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(final MethodArgumentNotValidException erro, final HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(getStandardError(HttpStatus.BAD_REQUEST.value(), "Erro de Validação", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(LoginInvalido.class)
    public ResponseEntity<StandardError> loginInvalidoException(final LoginInvalido erro, final HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(getStandardError(HttpStatus.UNAUTHORIZED.value(), "Erro de Autenticação", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(TokenInvalido.class)
    public ResponseEntity<StandardError> tokenInvalidoException(final TokenInvalido erro, final HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(getStandardError(HttpStatus.UNAUTHORIZED.value(), "Erro de Autenticação", erro.getMessage(), request.getRequestURI()));
    }

    private StandardError getStandardError(Integer status, String tipoErro, String mensagem, String uri){

        var erro = new StandardError();

        erro.setTimestamp(Instant.now());
        erro.setStatus(status);
        erro.setError(tipoErro);
        erro.setMessage(mensagem);
        erro.setPath(uri);

        return erro;
    }
}
