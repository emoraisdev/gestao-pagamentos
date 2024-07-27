package br.com.fiap.msclientes.model.dto;

public record ClienteResponseDTO(
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
