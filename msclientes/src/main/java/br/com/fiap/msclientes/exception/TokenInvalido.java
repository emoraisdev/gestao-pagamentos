package br.com.fiap.msclientes.exception;

public class TokenInvalido extends RuntimeException{

    public TokenInvalido() {
        super("Token Inválido.");
    }
}