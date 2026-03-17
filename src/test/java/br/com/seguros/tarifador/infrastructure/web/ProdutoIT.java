package br.com.seguros.tarifador.infrastructure.web;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoIT {

    @Autowired
    MockMvc mvc;

    @Test
    void deveExecutarFluxoCrudCompleto() throws Exception {
        String createBody = """
        {
          "nome": "Seguro Auto",
          "categoria": "AUTO",
          "preco_base": 50.00
        }
        """;

        MvcResult created = mvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Seguro Auto"))
                .andExpect(jsonPath("$.categoria").value("AUTO"))
                .andExpect(jsonPath("$.precoTarifado").value(55.25))
                .andReturn();

        String response = created.getResponse().getContentAsString();
        String id = JsonPath.read(response, "$.id");

        mvc.perform(get("/produtos/{id}", UUID.fromString(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Seguro Auto"));

        String updateBody = """
        {
          "nome": "Seguro Viagem Plus",
          "categoria": "VIAGEM",
          "preco_base": 75.00
        }
        """;

        mvc.perform(put("/produtos/{id}", UUID.fromString(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Seguro Viagem Plus"))
                .andExpect(jsonPath("$.categoria").value("VIAGEM"))
                .andExpect(jsonPath("$.precoTarifado").value(80.25));

        mvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mvc.perform(delete("/produtos/{id}", UUID.fromString(id)))
                .andExpect(status().isNoContent());

        mvc.perform(get("/produtos/{id}", UUID.fromString(id)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400QuandoPayloadInvalido() throws Exception {
        String invalidBody = """
        {
          "nome": "",
          "categoria": null,
          "preco_base": 0
        }
        """;

        mvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.errors").exists());
    }
}
