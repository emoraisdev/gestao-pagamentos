package br.com.fiap.mscartoes.service;

import br.com.fiap.mscartoes.dto.CartaoDTO;


public interface CartaoService {


    public void criar(CartaoDTO cartaoDTO);

    public CartaoDTO consultar(String cpf, String cartao);

}
