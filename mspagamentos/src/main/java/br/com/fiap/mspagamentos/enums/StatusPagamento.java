package br.com.fiap.mspagamentos.enums;

import lombok.Getter;

@Getter
public enum StatusPagamento {

    APROVADO("aprovado"),
    REPROVADO("reprovado"),
    EM_ANALISE("em_analise");

    private String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }
}
