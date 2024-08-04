package br.com.fiap.mscartoes.exception;

public class BusinessException extends RuntimeException{

    public BusinessException(String mensagem) {
        super(mensagem);
    }
}
