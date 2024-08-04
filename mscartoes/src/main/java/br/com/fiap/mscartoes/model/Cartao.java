package br.com.fiap.mscartoes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private Long limite;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String data_validade;

    @Column(nullable = false)
    private String cvv;

}
