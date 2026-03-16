package br.com.seguros.tarifador.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoIT {

    @Autowired
    MockMvc mvc;

    @Test
    void deveCriarECalcularPrecoTarifado() throws Exception {
        String body = """
      {
        "nome": "Seguro Auto",
        "categoria": "AUTO",
        "preco_base": 50.00
      }
    """;

        mvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.precoTarifado").value(55.25));
    }

}
