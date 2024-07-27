package br.com.fiap.msclientes.exception;

public class ParameterNotFoundException extends RuntimeException{

    public ParameterNotFoundException(String param) {
        super("O Parâmetro %s é obrigatório.".formatted(param));
    }
}
