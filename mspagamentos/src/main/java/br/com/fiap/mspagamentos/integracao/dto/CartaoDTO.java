package br.com.fiap.mspagamentos.integracao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartaoDTO {

    private String cpf;

    private BigDecimal limite;

    private String numero;

    @JsonProperty("data_validade")
    private String dataValidade;

    private String cvv;
}
