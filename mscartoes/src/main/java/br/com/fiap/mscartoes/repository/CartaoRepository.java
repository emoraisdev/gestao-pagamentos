package br.com.fiap.mscartoes.repository;

import br.com.fiap.mscartoes.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, String> {

    long countByCpf(String cpf);
    Optional<Cartao> findByCpfAndNumero(String cpf, String numero);
}
