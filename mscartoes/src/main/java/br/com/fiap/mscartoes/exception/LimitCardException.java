package br.com.fiap.mscartoes.exception;

public class LimitCardException extends RuntimeException{

    public LimitCardException() {
        super("Limite máximo de cartões por cliente atingido");
    }
}
