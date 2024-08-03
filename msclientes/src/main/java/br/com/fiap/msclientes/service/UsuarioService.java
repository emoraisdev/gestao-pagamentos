package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.exception.EntityNotFoundException;
import br.com.fiap.msclientes.model.Cliente;
import br.com.fiap.msclientes.model.Usuario;
import br.com.fiap.msclientes.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Optional<Usuario> getByUsuario(String usuario){
       return usuarioRepository.findByUsuario(usuario);
    }
}
