package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.model.Usuario;
import br.com.fiap.msclientes.model.dto.LoginDTO;
import br.com.fiap.msclientes.model.dto.TokenDTO;

public interface AutenticacaoService {

    TokenDTO logar(LoginDTO login);

    Usuario validarToken(String token);
}
