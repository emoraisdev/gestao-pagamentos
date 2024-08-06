package br.com.fiap.mspagamentos.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record ResponsePagamentoDTO(
        BigDecimal valor,
        String descricao,
        @JsonProperty("metodo_pagamento")
        String metodoPagamento,
        String status
) {
}
