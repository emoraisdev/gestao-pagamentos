package br.com.fiap.mspagamentos.model;

import br.com.fiap.mspagamentos.enums.MetodoPagamento;
import br.com.fiap.mspagamentos.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String numeroCartao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private MetodoPagamento metodoPagamento;

    @Column(nullable = false)
    private StatusPagamento status;
}
