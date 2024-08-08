package exception;

import static org.junit.Assert.assertEquals;

import br.com.fiap.gateway.exception.StandardError;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

public class StandardErrorTest {

    private StandardError standardError;

    @Before
    public void setUp() {
        standardError = new StandardError();
    }

    @Test
    public void testStandardErrorGettersAndSetters() {
        Instant timestamp = Instant.now();
        Integer status = 404;
        String error = "Not Found";
        String message = "teste";
        String path = "/api/resource";

        standardError.setTimestamp(timestamp);
        standardError.setStatus(status);
        standardError.setError(error);
        standardError.setMessage(message);
        standardError.setPath(path);

        assertEquals(timestamp, standardError.getTimestamp());
        assertEquals(status, standardError.getStatus());
        assertEquals(error, standardError.getError());
        assertEquals(message, standardError.getMessage());
        assertEquals(path, standardError.getPath());
    }

    @Test
    public void testAllArgsConstructor() {
        Instant timestamp = Instant.now();
        Integer status = 500;
        String error = "Internal Server Error";
        String message = "teste";
        String path = "/api/error";

        StandardError errorInstance = new StandardError(timestamp, status, error, message, path);

        assertEquals(timestamp, errorInstance.getTimestamp());
        assertEquals(status, errorInstance.getStatus());
        assertEquals(error, errorInstance.getError());
        assertEquals(message, errorInstance.getMessage());
        assertEquals(path, errorInstance.getPath());
    }
}