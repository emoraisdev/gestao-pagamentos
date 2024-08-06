package br.com.fiap.mspagamentos.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record RegistrarPagamentoDTO(

        @NotNull @CPF
        String cpf,
        @NotNull(message = "numero do cartao deve ser informado")
        @Pattern(regexp = "\\d{4} \\d{4} \\d{4} \\d{4}", message = "numero do cartao deve estar no formato '0000 0000 0000 0000'")
        String numero,
        @NotNull(message = "data de validade deve ser informado")
        @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "data de validade deve estar no formato 'MM/YY'")
        @JsonProperty("data_validade")
        String dataValidade,

        @NotNull(message = "cvv deve ser informado")
        @Size(min = 3, max = 3, message = "cvv deve ter exatamente 3 dígitos")
        @Pattern(regexp = "\\d{3}", message = "cvv deve conter apenas dígitos")
        String cvv,

        @NotNull
        BigDecimal valor
) {
}
