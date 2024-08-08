package br.com.fiap.mspagamentos.controller;

import br.com.fiap.mspagamentos.controller.dto.RegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponsePagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponseRegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@AllArgsConstructor
public class PagamentosController {

    private PagamentoService service;

    @PostMapping
    private ResponseEntity<ResponseRegistrarPagamentoDTO> registrar(@Valid @RequestBody RegistrarPagamentoDTO dto){

        var retorno = service.registrar(dto);

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @GetMapping("cliente/{cpf}")
    private ResponseEntity<List<ResponsePagamentoDTO>> consultarPorCliente(@PathVariable String cpf){

        var retorno = service.consultarPorCliente(cpf);

        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }
}
