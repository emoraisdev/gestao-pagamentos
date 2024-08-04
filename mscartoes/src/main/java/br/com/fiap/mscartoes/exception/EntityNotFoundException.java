package br.com.fiap.mscartoes.exception;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String entidade) {
        super("%s não encontrado.".formatted(entidade));
    }
}
