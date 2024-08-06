package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.controller.dto.RegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponsePagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponseRegistrarPagamentoDTO;

import java.util.List;

public interface PagamentoService {

    ResponseRegistrarPagamentoDTO registrar(RegistrarPagamentoDTO dto);

    List<ResponsePagamentoDTO> consultarPorCliente(String cpf);
}
