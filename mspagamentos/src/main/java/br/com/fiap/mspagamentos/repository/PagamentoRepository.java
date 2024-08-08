package br.com.fiap.mspagamentos.repository;

import br.com.fiap.mspagamentos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {


    @Query("SELECT COALESCE(SUM(p.valor), 0) FROM Pagamento p WHERE p.cpf = :cpf AND p.numeroCartao = :numeroCartao")
    Optional<BigDecimal> getSumValorByCpfAndNumero(@Param("cpf") String cpf,
                                                   @Param("numeroCartao") String numeroCartao);

    List<Pagamento> getPagamentoByCpf(String cpf);
}
