package br.com.fiap.mscartoes.service.implementation;

import br.com.fiap.mscartoes.dto.CartaoDTO;
import br.com.fiap.mscartoes.dto.ClienteResponseDTO;
import br.com.fiap.mscartoes.exception.BusinessException;
import br.com.fiap.mscartoes.exception.EntityNotFoundException;
import br.com.fiap.mscartoes.exception.LimitCardException;
import br.com.fiap.mscartoes.exception.NotAutorizedException;
import br.com.fiap.mscartoes.model.Cartao;
import br.com.fiap.mscartoes.repository.CartaoRepository;
import br.com.fiap.mscartoes.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CartaoServiceImpl implements CartaoService {

    private final CartaoRepository cartaoRepository;

    private final RestTemplate restTemplate;

    @Value("${api.mscliente.url}")
    private String urlMSClientes;

    @Autowired
    public CartaoServiceImpl(CartaoRepository cartaoRepository, RestTemplate restTemplate) {
        this.cartaoRepository = cartaoRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public void criar(CartaoDTO cartaoDTO) {

        if(isClienteExistente(cartaoDTO.getCpf())){
            if(isCartaoExistenteParaMesmoCpf(cartaoDTO)){
                throw new BusinessException("O mesmo numero de cartão já existe para o CPF informado");
            }
            if(isQuantidadeCartoesExcedidaPorCPF(cartaoDTO)) {
                throw new LimitCardException();
            }

            cartaoRepository.save(buildCartao(cartaoDTO));
        }else {
            throw new NotAutorizedException();
        }
    }

    private boolean isQuantidadeCartoesExcedidaPorCPF(CartaoDTO cartaoDTO) {
        long amountCards = cartaoRepository.countByCpf(cartaoDTO.getCpf());
        return amountCards >= 2;
    }

    private boolean isCartaoExistenteParaMesmoCpf(CartaoDTO cartaoDTO) {
        return cartaoRepository.findByCpfAndNumero(cartaoDTO.getCpf(), cartaoDTO.getNumero()).isPresent();
    }

    private boolean isClienteExistente(String cpf) {
        try {
            ResponseEntity<ClienteResponseDTO> responseEntity = restTemplate.getForEntity(urlMSClientes + "/cpf/" + cpf, ClienteResponseDTO.class);
            if (responseEntity.getBody() != null) {
                return true;
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new BusinessException(String.format("Cliente com cpf %s não encontrado.", cpf));
        } catch (HttpClientErrorException.BadRequest e) {
            throw new BusinessException("Erro ao buscar cliente: " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            throw new BusinessException("Erro no servidor ao buscar Cliente: " + e.getResponseBodyAsString());
        }

        return false;
    }

    @Override
    public CartaoDTO consultar(String cpf, String numeroCartao) {
        Optional<Cartao> cartao = Optional.ofNullable(cartaoRepository.findByCpfAndNumero(cpf, numeroCartao)
                .orElseThrow(() -> new EntityNotFoundException(Cartao.class.getSimpleName())));

        return buildCartaoDTO(cartao.get());
    }

    private Cartao buildCartao(CartaoDTO cartaoDTO) {
        return Cartao.builder()
                .cpf(cartaoDTO.getCpf())
                .limite(cartaoDTO.getLimite())
                .numero(cartaoDTO.getNumero())
                .data_validade(cartaoDTO.getData_validade())
                .cvv(cartaoDTO.getCvv())
                .build();
    }

    private CartaoDTO buildCartaoDTO(Cartao cartao) {
        return CartaoDTO.builder()
                .cpf(cartao.getCpf())
                .limite(cartao.getLimite())
                .numero(cartao.getNumero())
                .data_validade(cartao.getData_validade())
                .cvv(cartao.getCvv())
                .build();
    }
}
