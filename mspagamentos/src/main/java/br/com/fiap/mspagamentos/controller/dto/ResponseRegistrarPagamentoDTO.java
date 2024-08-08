package br.com.fiap.mspagamentos.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseRegistrarPagamentoDTO(
        @JsonProperty("chave_pagamento")
        String chavePagamento
) {
}
