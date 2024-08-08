package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.exception.LoginInvalido;
import br.com.fiap.msclientes.exception.TokenInvalido;
import br.com.fiap.msclientes.model.Usuario;
import br.com.fiap.msclientes.model.dto.LoginDTO;
import br.com.fiap.msclientes.security.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AutenticacaoServiceImplTest {

    @InjectMocks
    private AutenticacaoServiceImpl autenticacaoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoderService encoderService;

    @Mock
    private TokenService tokenService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogar_Success() {
        String usuario = "testUser";
        String senha = "testPassword";
        String encodedSenha = "encodedPassword";
        String token = "token";
        LoginDTO loginDTO = new LoginDTO(usuario, senha);
        Usuario usuarioMock = new Usuario();
        usuarioMock.setUsuario(usuario);
        usuarioMock.setSenha(encodedSenha);

        when(usuarioService.getByUsuario(usuario)).thenReturn(Optional.of(usuarioMock));
        when(encoderService.matches(senha, encodedSenha)).thenReturn(true);
        when(tokenService.generateToken(usuarioMock)).thenReturn(token);

        var result = autenticacaoService.logar(loginDTO);

        assertEquals(token, result.token());
        verify(usuarioService).getByUsuario(usuario);
        verify(encoderService).matches(senha, encodedSenha);
        verify(tokenService).generateToken(usuarioMock);
    }

    @Test
    public void testLogar_UsuarioNaoEncontrado() {
        String usuario = "testUser";
        String senha = "testPassword";
        LoginDTO loginDTO = new LoginDTO(usuario, senha);

        when(usuarioService.getByUsuario(usuario)).thenReturn(Optional.empty());

        assertThrows(LoginInvalido.class, () -> autenticacaoService.logar(loginDTO));
    }

    @Test
    public void testLogar_SenhaIncorreta() {
        String usuario = "testUser";
        String senha = "testPassword";
        String encodedSenha = "encodedPassword";
        LoginDTO loginDTO = new LoginDTO(usuario, senha);
        Usuario usuarioMock = new Usuario();
        usuarioMock.setUsuario(usuario);
        usuarioMock.setSenha(encodedSenha);

        when(usuarioService.getByUsuario(usuario)).thenReturn(Optional.of(usuarioMock));
        when(encoderService.matches(senha, encodedSenha)).thenReturn(false);

        assertThrows(LoginInvalido.class, () -> autenticacaoService.logar(loginDTO));
    }

    @Test
    public void testValidarToken_Success() {
        String token = "validToken";
        String usuario = "testUser";
        Usuario usuarioMock = new Usuario();
        usuarioMock.setUsuario(usuario);

        when(tokenService.validateToken(token)).thenReturn(usuario);
        when(usuarioService.getByUsuario(usuario)).thenReturn(Optional.of(usuarioMock));

        Usuario result = autenticacaoService.validarToken(token);

        assertEquals(usuarioMock, result);
        verify(tokenService).validateToken(token);
        verify(usuarioService).getByUsuario(usuario);
    }

    @Test
    public void testValidarToken_TokenInvalido() {
        String token = "invalidToken";
        String usuario = "testUser";

        when(tokenService.validateToken(token)).thenReturn(usuario);
        when(usuarioService.getByUsuario(usuario)).thenReturn(Optional.empty());

        assertThrows(TokenInvalido.class, () -> autenticacaoService.validarToken(token));
    }
}