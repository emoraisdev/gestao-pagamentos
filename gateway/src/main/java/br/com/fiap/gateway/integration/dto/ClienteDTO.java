package br.com.fiap.gateway.integration.dto;

public record ClienteDTO(
        Long id,
        String cpf,
        String nome,
        String email,
        String telefone,
        String rua,
        String cidade,
        String estado,
        String cep,
        String pais
) {
}
