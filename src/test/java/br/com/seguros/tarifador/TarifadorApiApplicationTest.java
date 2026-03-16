package br.com.seguros.tarifador;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TarifadorApiApplicationTest {

    @Test
    void contextLoads() {
        // Verifica se o contexto Spring carrega corretamente
    }

    @Test
    void mainMethodRuns() {
        TarifadorApiApplication.main(new String[]{});
    }
}