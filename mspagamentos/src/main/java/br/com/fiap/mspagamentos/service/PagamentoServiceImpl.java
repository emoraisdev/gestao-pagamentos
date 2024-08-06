package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.controller.dto.RegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponsePagamentoDTO;
import br.com.fiap.mspagamentos.controller.dto.ResponseRegistrarPagamentoDTO;
import br.com.fiap.mspagamentos.enums.MetodoPagamento;
import br.com.fiap.mspagamentos.enums.StatusPagamento;
import br.com.fiap.mspagamentos.exception.BusinessException;
import br.com.fiap.mspagamentos.exception.SaldoInsuficienteException;
import br.com.fiap.mspagamentos.integracao.CartaoServiceApi;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.repository.PagamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class PagamentoServiceImpl implements PagamentoService{

    private final PagamentoRepository repo;

    private final CartaoServiceApi cartaoApi;

    @Override
    public ResponseRegistrarPagamentoDTO registrar(RegistrarPagamentoDTO dto) {

        validarOperacao(dto);

        var pagamento = new Pagamento();
        pagamento.setMetodoPagamento(MetodoPagamento.CREDITO);
        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setValor(dto.valor());
        pagamento.setCpf(dto.cpf());
        pagamento.setNumeroCartao(dto.numero());
        pagamento.setDescricao("Compra de produto X");

        var pagamentoSalvo = repo.save(pagamento);

        return new ResponseRegistrarPagamentoDTO(pagamentoSalvo.getId());
    }

    private void validarOperacao(RegistrarPagamentoDTO dto){

        var cartao = cartaoApi.consultarCartao(dto.cpf(), dto.numero());

        if (cartao == null) {
            throw new BusinessException("Cartão Não Encontrado.");
        }

        if (!cartao.getDataValidade().equals(dto.dataValidade())) {
            throw new BusinessException("Data de Validade Inválida.");
        }

        if (!cartao.getCvv().equals(dto.cvv())) {
            throw new BusinessException("CVV Inválido.");
        }

        var valorUtilizado = repo.getSumValorByCpfAndNumero(dto.cpf(), dto.numero())
                .orElse(BigDecimal.ZERO);

        var saldo = cartao.getLimite().subtract(valorUtilizado);

        if (dto.valor().compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException("Saldo Insuficiente: " + saldo.toString());
        }

    }

    @Override
    public List<ResponsePagamentoDTO> consultarPorCliente(String cpf) {
        return null;
    }
}
