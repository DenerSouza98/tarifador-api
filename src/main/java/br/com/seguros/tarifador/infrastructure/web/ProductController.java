package br.com.seguros.tarifador.infrastructure.web;

import br.com.seguros.tarifador.application.service.PricingService;
import br.com.seguros.tarifador.domain.model.Product;
import br.com.seguros.tarifador.infrastructure.web.dto.ProductRequest;
import br.com.seguros.tarifador.infrastructure.web.dto.ProductResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
@Slf4j
public class ProductController {

    private final PricingService service;
    private final Counter produtosCriados;
    private final Counter produtosAtualizados;

    public ProductController(PricingService service, MeterRegistry registry) {
        this.service = service;
        this.produtosCriados = Counter.builder("produtos_criados_total")
                .description("Quantidade de produtos criados")
                .register(registry);
        this.produtosAtualizados = Counter.builder("produtos_atualizados_total")
                .description("Quantidade de produtos atualizados")
                .register(registry);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse creatadProduct(@Valid @RequestBody ProductRequest req) {
        log.debug("Recebida requisição para criar produto: nome={}, categoria={}, precoBase={}",
                req.getNome(), req.getCategoria(), req.getPrecoBase());
        Product newProduct = new Product(null, req.getNome(), req.getCategoria(), req.getPrecoBase(), null);
        Product salvo = service.createProduct(newProduct);
        produtosCriados.increment();
        return toResponse(salvo);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequest req) {
        log.debug("Recebida requisição para atualizar produto: id={}, nome={}, categoria={}, precoBase={}",
                id, req.getNome(), req.getCategoria(), req.getPrecoBase());
        Product alteracoes = new Product(null, req.getNome(), req.getCategoria(), req.getPrecoBase(), null);
        Product salvo = service.updateProduct(id, alteracoes);
        produtosAtualizados.increment();
        return toResponse(salvo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        log.debug("Recebida requisição para deletar produto: id={}", id);
        service.deleteProduct(id);
    }

    @GetMapping("/{id}")
    public ProductResponse searchProduct(@PathVariable UUID id) {
        return toResponse(service.searchProduct(id));
    }

    @GetMapping
    public List<ProductResponse> listProduct() {
        return service.listProduct().stream().map(this::toResponse).toList();
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getNome(), p.getCategoria(), p.getPrecoBase(), p.getPrecoTarifado());
    }
}
