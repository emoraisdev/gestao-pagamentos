package br.com.fiap.msclientes.controller;

import br.com.fiap.msclientes.model.Usuario;
import br.com.fiap.msclientes.model.dto.ClienteResponseDTO;
import br.com.fiap.msclientes.model.dto.LoginDTO;
import br.com.fiap.msclientes.model.dto.TokenDTO;
import br.com.fiap.msclientes.service.AutenticacaoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autenticacao")
@AllArgsConstructor
public class AutenticacaoController {

    private final AutenticacaoService service;

    @PostMapping
    public ResponseEntity<TokenDTO> logar(@Valid @RequestBody LoginDTO dto){

        var token = service.logar(dto);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/validar")
    public ResponseEntity<Usuario> validar(@RequestBody TokenDTO token){

        var cliente = service.validarToken(token.token());

        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }
}
