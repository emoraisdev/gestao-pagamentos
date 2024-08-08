package exception;

import static org.junit.Assert.assertEquals;

import br.com.fiap.gateway.exception.RequestException;
import org.junit.Test;

public class RequestExceptionTest {

    @Test
    public void testRequestExceptionWithBody() {
        var corpo = "Corpo da resposta de erro";

        var exception = RequestException.builder()
                .body(corpo)
                .build();

        assertEquals(corpo, exception.getBody());
        assertEquals("", exception.getMessage());
    }
}