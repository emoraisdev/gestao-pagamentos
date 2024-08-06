package br.com.fiap.mspagamentos.enums;

import lombok.Getter;

@Getter
public enum MetodoPagamento {

    CREDITO("cartao_credito"),
    DEBITO("cartao_debito"),
    PIX("pix");

    private String descricao;

    MetodoPagamento(String descricao) {
        this.descricao = descricao;
    }
}
