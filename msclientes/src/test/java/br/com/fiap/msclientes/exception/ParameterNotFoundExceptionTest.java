package br.com.fiap.msclientes.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParameterNotFoundExceptionTest {

    @Test
    public void testParameterNotFoundExceptionMessage() {
        String paramName = "username";
        String expectedMessage = "O Parâmetro username é obrigatório.";

        try {
            throw new ParameterNotFoundException(paramName);
        } catch (ParameterNotFoundException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}