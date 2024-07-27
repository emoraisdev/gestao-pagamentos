package br.com.fiap.msclientes.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClienteIncluirResponseDTO(
        @JsonProperty("id_cliente")
        long idCliente
) {
}
