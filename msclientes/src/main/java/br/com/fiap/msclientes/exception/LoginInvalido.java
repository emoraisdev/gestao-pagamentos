package br.com.fiap.msclientes.exception;

public class LoginInvalido extends RuntimeException{

    public LoginInvalido() {
        super("Usuario e/ou Senha Inválidos.");
    }
}