package br.com.fiap.msclientes.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteDTO(
        @NotBlank(message = "O CPF não pode estar em branco")
        @CPF(message = "O CPF é inválido")
        String cpf,
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,
        @NotBlank(message = "O E-mail não pode estar em branco")
        @Email(message = "E-mail inválido")
        String email,
        @NotBlank(message = "O Telefone não pode estar em branco")
        String telefone,
        @NotBlank(message = "A Rua não pode estar em branco")
        String rua,
        @NotBlank(message = "A Cidade não pode estar em branco")
        String cidade,
        @NotBlank(message = "O Estado não pode estar em branco")
        String estado,
        @NotBlank(message = "O CEP não pode estar em branco")
        String cep,
        @NotBlank(message = "O País não pode estar em branco")
        String pais,
        @Size(min = 6, message = "A Senha deve ter pelo menos 6 caracteres")
        @Size(max = 12, message = "A Senha deve ter no máximo 12 caracteres")
        String senha,

        @Size(min = 6, message = "A Senha de confirmação deve ter pelo menos 6 caracteres")
        @Size(max = 12, message = "A Senha de confirmação deve ter no máximo 12 caracteres")
        String confirmacaoSenha
) {
}
