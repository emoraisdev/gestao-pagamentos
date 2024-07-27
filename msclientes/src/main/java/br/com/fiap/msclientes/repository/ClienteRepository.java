package br.com.fiap.msclientes.repository;

import br.com.fiap.msclientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> getClienteByEmail(String email);
}
