package br.com.fiap.mscartoes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartaoDTO {


    @NotNull(message = "cpf deve ser informado")
    @Size(min = 11, max = 11, message = "cpf deve ter exatamente 11 dígitos")
    @Pattern(regexp = "\\d{11}", message = "cpf deve conter apenas dígitos")
    private String cpf;


    @NotNull(message = "limite deve ser informado")
    @PositiveOrZero(message = "limite deve ser um valor positivo ou zero")
    private Long limite;


    @NotNull(message = "numero do cartao deve ser informado")
    @Pattern(regexp = "\\d{4} \\d{4} \\d{4} \\d{4}", message = "numero do cartao deve estar no formato '0000 0000 0000 0000'")
    private String numero;


    @NotNull(message = "data de validade deve ser informado")
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "data de validade deve estar no formato 'MM/YY'")
    private String data_validade;


    @NotNull(message = "cvv deve ser informado")
    @Size(min = 3, max = 3, message = "cvv deve ter exatamente 3 dígitos")
    @Pattern(regexp = "\\d{3}", message = "cvv deve conter apenas dígitos")
    private String cvv;
}
