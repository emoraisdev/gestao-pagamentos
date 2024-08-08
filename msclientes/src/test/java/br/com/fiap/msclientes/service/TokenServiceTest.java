package br.com.fiap.msclientes.service;

import br.com.fiap.msclientes.security.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateToken_ValidToken() {
        String secret = "testSecret";
        tokenService.secret = secret;
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer("auth-api")
                .withSubject("testUser")
                .sign(algorithm);

        String user = tokenService.validateToken(token);

        assertEquals("testUser", user);
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String secret = "testSecret";
        tokenService.secret = secret;

        String user = tokenService.validateToken("invalidToken");

        assertNull(user);
    }
}