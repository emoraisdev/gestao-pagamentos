package exception;

import static org.junit.Assert.assertEquals;

import br.com.fiap.gateway.exception.BusinessException;
import org.junit.Test;

public class BusinessExceptionTest {

    @Test
    public void testBusinessException() {
        var mensagem = "Erro de negócio ocorreu";

        var exception = new BusinessException(mensagem);

        assertEquals(mensagem, exception.getMessage());
    }
}