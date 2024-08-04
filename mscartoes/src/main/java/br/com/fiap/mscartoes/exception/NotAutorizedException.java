package br.com.fiap.mscartoes.exception;

public class NotAutorizedException extends RuntimeException{

    public NotAutorizedException() {
        super("Cliente não autorizado para essa transação");
    }
}
