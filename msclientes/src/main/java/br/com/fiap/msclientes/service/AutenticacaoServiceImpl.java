package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.exception.LoginInvalido;
import br.com.fiap.msclientes.exception.TokenInvalido;
import br.com.fiap.msclientes.model.Usuario;
import br.com.fiap.msclientes.model.dto.LoginDTO;
import br.com.fiap.msclientes.model.dto.TokenDTO;
import br.com.fiap.msclientes.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutenticacaoServiceImpl implements AutenticacaoService{

    private final UsuarioService usuarioService;

    private final PasswordEncoderService encoderService;

    private final TokenService tokenService;

    @Override
    public TokenDTO logar(LoginDTO login) {
        var usuario = usuarioService.getByUsuario(login.usuario()).orElseThrow(LoginInvalido::new);

        if (encoderService.matches(login.senha(), usuario.getSenha())){
            return new TokenDTO(tokenService.generateToken(usuario));
        } else {
            throw new LoginInvalido();
        }
    }

    @Override
    public Usuario validarToken(String token) {
        String usuario = tokenService.validateToken(token);

        return usuarioService.getByUsuario(usuario).orElseThrow(TokenInvalido::new);
    }
}
