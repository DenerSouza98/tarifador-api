# Tarifador API

API REST para cadastro e tarifação automática de produtos de seguros com excelência técnica em qualidade de código, arquitetura limpa, testes 100% e observabilidade completa.

---

## 🚀 Quick Start

```bash
docker compose up --build -d
```
### URLs principais:

API: http://localhost:8080

Health: http://localhost:8080/actuator/health

Métricas: http://localhost:8080/actuator/prometheus

Prometheus UI: http://localhost:9090

Grafana: http://localhost:3000 (admin/admin)

Zipkin: http://localhost:9412  

H2 Console: http://localhost:8080/h2-console (jdbc:h2:mem:tarifador, sa, sa)

### Objetivos do projeto:

- Operações CRUD de produtos de seguro com cálculo automático de precoTarifado baseado em alíquotas de IOF/PIS/COFINS por categoria.

### 🏗️ Arquitetura Clean Architecture
```
domain/          ← Regras de negócio puras
├── model/       (Categoria, Product)
└── pricing/     (TaxPolicy, DefaultTaxPolicy, TaxPolicyRegistry)

application/     ← Casos de uso
├── ports/       (ProductRepository interface)
└── service/     (PricingService)

infrastructure/  ← Adaptadores
├── persistence/ (JPA, Mapper, Adapter)
└── web/         (Controller, GlobalExceptionHandler, DTOs)
```
### 🎯 Design Patterns

- **Strategy**: TaxPolicy + DefaultTaxPolicy — diferentes estratégias por categoria
- **Repository**: ProductRepository interface para abstração de persistência.
- **Adapter**: ProductRepositoryAdapter — desacopla domínio de JPA
- **Mapper**: ProductMapper — conversão domínio ↔ JPA ↔ HTTP
  - **DTO**: ProductRequest, ProductResponse — protege modelo de domínio 
---------
### ✅ SOLID Aplicado
- **SRP**: Cada classe: uma responsabilidade (Service, Mapper, Handler, Controller)
- **OCP**: Nova TaxPolicy sem alterar código existente
- **LSP**: ProductRepositoryAdapter substitui ProductRepository completamente.
- **ISP**: Interfaces pequenas e focadas (ProductRepository, TaxPolicy).
- **DIP**: DPricingService depende de abstrações, não de JPA concreto.
---------

### 🧪 Testes e Cobertura
- Cobertura: 100% (11/11 classes, 48/48 métodos, 162/162 linhas, 4/4 branches)

### Teste:
- 9 unitários (PricingService, Controller, Exception Handler, Policies, Models, Adapter, Entity)
- 2 integração (ProdutoIT: CRUD completo + validação 400)

### Executar:
```
mvn clean test           # unitários
mvn clean verify         # + integração + JaCoCo gate
mvn jacoco:report        # relatório HTML
```
### 📊 Observabilidade Completa
- Métricas (Prometheus)

- Expostas via Actuator: http://localhost:8080/actuator/prometheus

### Queries Prometheus:
```
# API UP?
up{job="tarifador-api"}

# Taxa de criação
rate(produtos_criados_total[5m])

# Taxa de atualização
rate(produtos_atualizados_total[5m])

# Taxa de erros
rate(pricing_errors_total[5m])

# Latência p95
histogram_quantile(0.95, sum by (le) (
  rate(http_server_requests_seconds_bucket[5m])
))

# Latência p99
histogram_quantile(0.99, sum by (le) (
  rate(http_server_requests_seconds_bucket[5m])
))

# Total requisições por endpoint
sum by (uri, status) (
  rate(http_server_requests_seconds_count[5m])
)
```
### Tracing (Zipkin)
- Rastreio completo de requisições, visibilidade de latências e falhas.
- Acessar: http://localhost:9412
- Visualizar spans, identificar gargalos e erros.

### Todos com traceId e spanId:
```
INFO [tarifador-api,abc123,def456] Criando produto: categoria=AUTO, precoBase=50.00
```

### 🛠️ Tecnologias
Java 17 • Spring Boot 3.2.5 • Spring Web • Spring Data JPA • H2 • Jakarta Validation • Micrometer • Prometheus • Zipkin • JUnit 5 • Mockito • JaCoCo • Docker Compose






