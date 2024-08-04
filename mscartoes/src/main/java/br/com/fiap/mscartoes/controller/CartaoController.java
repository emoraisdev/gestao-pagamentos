package br.com.fiap.mscartoes.controller;

import br.com.fiap.mscartoes.dto.CartaoDTO;
import br.com.fiap.mscartoes.service.CartaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/cartao")
public class CartaoController {

    private final CartaoService cartaoService;

    @Autowired
    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @PostMapping()
    public ResponseEntity<Void> criar(@Valid @RequestBody CartaoDTO cartaoDTO) {
        cartaoService.criar(cartaoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{cpf}/{cartao}")
    public ResponseEntity<CartaoDTO> consultar(@PathVariable String cpf, @PathVariable String cartao) {
        return ResponseEntity.ok(cartaoService.consultar(cpf, cartao));
    }
}
