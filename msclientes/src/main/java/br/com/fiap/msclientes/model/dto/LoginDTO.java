package br.com.fiap.msclientes.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank
        String usuario,
        @NotBlank
        String senha
) {
}
