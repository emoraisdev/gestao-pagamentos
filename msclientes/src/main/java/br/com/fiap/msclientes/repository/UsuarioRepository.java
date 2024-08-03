package br.com.fiap.msclientes.repository;

import br.com.fiap.msclientes.model.Cliente;
import br.com.fiap.msclientes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {
    Optional<Usuario> findByUsuario(String usuario);
}
