package br.com.fiap.msclientes.exception;

public class LoginInvalido extends RuntimeException{

    public LoginInvalido() {
        super("E-mail e/ou Senha Inválidos.");
    }
}