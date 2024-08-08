package br.com.fiap.msclientes.service;

import org.junit.Before;
import org.testng.annotations.Test;

import static org.junit.Assert.assertTrue;


public class PasswordEncoderServiceTest {

    private PasswordEncoderService passwordEncoderService;

    @Before
    public void setUp() {
        passwordEncoderService = new PasswordEncoderService();
    }

    @Test
    public void testEncodePassword() {
        String rawPassword = "mySecretPassword";

        String encodedPassword = passwordEncoderService.encodePassword(rawPassword);

        assertTrue(encodedPassword != null && !encodedPassword.isEmpty());
    }
}