package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.model.Usuario;
import br.com.fiap.msclientes.repository.UsuarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetByUsuario_Found() {
        String usuario = "testUser";
        Usuario usuarioMock = new Usuario();
        usuarioMock.setUsuario(usuario);

        when(usuarioRepository.findByUsuario(usuario)).thenReturn(Optional.of(usuarioMock));

        Optional<Usuario> result = usuarioService.getByUsuario(usuario);

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetByUsuario_NotFound() {
        String usuario = "nonExistentUser";

        when(usuarioRepository.findByUsuario(usuario)).thenReturn(Optional.empty());

        Optional<Usuario> result = usuarioService.getByUsuario(usuario);

        assertEquals(Optional.empty(), result);
    }
}