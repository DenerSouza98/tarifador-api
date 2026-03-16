package br.com.seguros.tarifador.infrastructure.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void deveRetornar404QuandoEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Produto não encontrado");

        ResponseEntity<?> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Produto não encontrado", body.get("message"));
    }

    @Test
    void deveRetornar400QuandoMethodArgumentNotValid() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError error1 = new FieldError("productRequest", "name", "não deve estar em branco");
        FieldError error2 = new FieldError("productRequest", "base_price", "deve ser maior que 0");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        ResponseEntity<?> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Validation error", body.get("message"));

        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertEquals("não deve estar em branco", errors.get("name"));
        assertEquals("deve ser maior que 0", errors.get("base_price"));
    }

    @Test
    void deveRetornar400QuandoConstraintViolation() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        when(ex.getMessage()).thenReturn("Constraint violation");

        ResponseEntity<?> response = handler.handleConstraint(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Constraint violation", body.get("message"));
    }

    @Test
    void deveRetornar500QuandoExceptionGenerica() {
        Exception ex = new RuntimeException("Erro inesperado");

        ResponseEntity<?> response = handler.handleGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Unexpected error", body.get("message"));
    }

    @Test
    void deveRetornar400ComCamposDuplicadosNoValidation() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError error1 = new FieldError("productRequest", "name", "erro 1");
        FieldError error2 = new FieldError("productRequest", "name", "erro 2");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        ResponseEntity<?> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        Map<String, String> errors = (Map<String, String>) body.get("errors");

        // Deve manter apenas o primeiro erro devido ao merge (a, b) -> a
        assertEquals("erro 1", errors.get("name"));
    }

}