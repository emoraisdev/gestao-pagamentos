package br.com.fiap.mscartoes.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> businessException(BusinessException erro, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(getStandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro na solicitação", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(LimitCardException.class)
    public ResponseEntity<StandardError> limitCardException(LimitCardException erro, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .body(getStandardError(HttpStatus.FORBIDDEN.value(), "Limite de cartões atingido", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFoundException(final EntityNotFoundException erro, final HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(getStandardError(HttpStatus.NOT_FOUND.value(), "Entidade Não Encontrada", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(NotAutorizedException.class)
    public ResponseEntity<StandardError> entityNotFoundException(final NotAutorizedException erro, final HttpServletRequest request){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(getStandardError(HttpStatus.UNAUTHORIZED.value(), "Não Autorizado", erro.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> entityNotFoundException(final MethodArgumentNotValidException erro, final HttpServletRequest request){
        AtomicReference<String> message = new AtomicReference<>("");
        erro.getBindingResult().getAllErrors().forEach(error ->
            message.set(error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(getStandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro na solicitação", message.get(), request.getRequestURI()));
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
