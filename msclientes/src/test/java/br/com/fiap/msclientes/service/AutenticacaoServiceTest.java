package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.exception.BusinessException;
import br.com.fiap.msclientes.exception.LoginInvalido;
import br.com.fiap.msclientes.model.Usuario;
import br.com.fiap.msclientes.model.dto.LoginDTO;
import br.com.fiap.msclientes.model.dto.TokenDTO;
import br.com.fiap.msclientes.repository.UsuarioRepository;
import br.com.fiap.msclientes.security.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AutenticacaoServiceTest {

    @InjectMocks
    private AutenticacaoServiceImpl autenticacaoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoderService encoderService;

    @Mock
    private TokenService tokenService;

    @Test
    void logarQuandoPassadoLoginCorreto() {
        LoginDTO loginDTO = new LoginDTO("UsuarioCorreto","SenhaCorreta");
        when(usuarioService.getByUsuario(loginDTO.usuario())).thenReturn(Optional.of(getUsarioCorreto()));
        when(encoderService.matches(loginDTO.senha(),"SenhaCorreta")).thenReturn(Boolean.TRUE);
        TokenDTO tokenDTO = autenticacaoService.logar(loginDTO);

        assertNotNull(tokenDTO);
    }

    @Test
    void logarQuandoPassadoLoginIncorreto() {
        LoginDTO loginDTO = new LoginDTO("UsuarioCorreto","SenhaIncorreta");
        when(usuarioService.getByUsuario(loginDTO.usuario())).thenReturn(Optional.of(getUsarioCorreto()));
        when(encoderService.matches(loginDTO.senha(),"SenhaCorreta")).thenReturn(Boolean.FALSE);

        assertThatExceptionOfType(LoginInvalido.class)
                .isThrownBy(() -> autenticacaoService.logar(loginDTO))
                .withMessage("Usuario e/ou Senha Inválidos.");
    }

    private Usuario getUsarioCorreto(){
        return new Usuario(1L,"UsuarioCorreto","SenhaCorreta");
    }
}
