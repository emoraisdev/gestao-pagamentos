package br.com.fiap.msclientes.controller;

import br.com.fiap.msclientes.model.dto.ClienteDTO;
import br.com.fiap.msclientes.model.dto.ClienteIncluirResponseDTO;
import br.com.fiap.msclientes.model.dto.ClienteResponseDTO;
import br.com.fiap.msclientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
@AllArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteIncluirResponseDTO> incluir(@Valid @RequestBody ClienteDTO dto){

        var resultado = service.incluir(dto);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<ClienteResponseDTO>> listar(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        var usuarios = service.listar(PageRequest.of(page, size));
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(path = "/by-email/{email}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ClienteResponseDTO> getByEmail(@PathVariable String email) {

        var usuario = service.getClienteByEmail(email);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}
