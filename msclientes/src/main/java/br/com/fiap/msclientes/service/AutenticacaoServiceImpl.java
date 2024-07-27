package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.exception.BusinessException;
import br.com.fiap.msclientes.exception.LoginInvalido;
import br.com.fiap.msclientes.exception.TokenInvalido;
import br.com.fiap.msclientes.model.dto.ClienteResponseDTO;
import br.com.fiap.msclientes.model.dto.LoginDTO;
import br.com.fiap.msclientes.model.dto.TokenDTO;
import br.com.fiap.msclientes.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutenticacaoServiceImpl implements AutenticacaoService{

    private final ClienteService clienteService;

    private final PasswordEncoderService encoderService;

    private final TokenService tokenService;

    @Override
    public TokenDTO logar(LoginDTO login) {
        var cliente = clienteService.getUsuarioEntidadeByEmail(login.usuario()).orElseThrow(LoginInvalido::new);

        if (encoderService.matches(login.senha(), cliente.getSenha())){
            return new TokenDTO(tokenService.generateToken(cliente));
        } else {
            throw new LoginInvalido();
        }
    }

    @Override
    public ClienteResponseDTO validarToken(String token) {
        String email = tokenService.validateToken(token);
        if (email == null) {
            throw new TokenInvalido();
        }

        return clienteService.getClienteByEmail(email);
    }
}
