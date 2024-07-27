package br.com.fiap.gateway.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestException extends RuntimeException{

    private String body;

    public RequestException(String body) {
        super("");
        this.body = body;
    }
}
