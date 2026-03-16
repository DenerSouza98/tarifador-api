package br.com.seguros.tarifador.domain.pricing;

import br.com.seguros.tarifador.domain.model.Categoria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class DefaultTaxPolicyTest {


    @Test
    @DisplayName("Deve calcular corretamente tarifa para categoria VIDA")
    void deveCalcularTarifaVida() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.VIDA);
        BigDecimal basePrice = new BigDecimal("100.00");

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        assertEquals(new BigDecimal("103.20"), result);
        assertEquals(2, result.scale());
    }

    @Test
    @DisplayName("Deve calcular corretamente tarifa para categoria AUTO")
    void deveCalcularTarifaAuto() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.AUTO);
        BigDecimal basePrice = new BigDecimal("50.00");

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        assertEquals(new BigDecimal("55.25"), result);
    }

    @Test
    @DisplayName("Deve calcular corretamente tarifa para categoria VIAGEM")
    void deveCalcularTarifaViagem() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.VIAGEM);
        BigDecimal basePrice = new BigDecimal("100.00");

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        assertEquals(new BigDecimal("107.00"), result);
    }

    @Test
    @DisplayName("Deve calcular corretamente tarifa para categoria RESIDENCIAL")
    void deveCalcularTarifaResidencial() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.RESIDENCIAL);
        BigDecimal basePrice = new BigDecimal("100.00");

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        assertEquals(new BigDecimal("107.00"), result);
    }

    @Test
    @DisplayName("Deve calcular corretamente tarifa para categoria PATRIMONIAL")
    void deveCalcularTarifaPatrimonial() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.PATRIMONIAL);
        BigDecimal basePrice = new BigDecimal("100.00");

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        assertEquals(new BigDecimal("108.00"), result);
    }

    @ParameterizedTest
    @CsvSource({
            "VIDA, 100.00, 103.20",
            "VIDA, 200.00, 206.40",
            "AUTO, 50.00, 55.25",
            "AUTO, 100.00, 110.50",
            "VIAGEM, 75.00, 80.25",
            "RESIDENCIAL, 150.00, 160.50",
            "PATRIMONIAL, 80.00, 86.40"
    })
    @DisplayName("Deve calcular tarifas corretamente para múltiplos cenários")
    void deveCalcularTarifasMultiplosValores(Categoria category, BigDecimal basePrice, BigDecimal expected) {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(category);

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Deve arredondar para 2 casas decimais usando HALF_UP")
    void deveArredondarCorretamente() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.VIDA);
        BigDecimal basePrice = new BigDecimal("33.33");

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        // 33.33 + (33.33 × 0.01) + (33.33 × 0.022) + 0 = 33.33 + 0.3333 + 0.73326 = 34.39656
        // Arredondado para 2 casas: 34.40
        assertEquals(new BigDecimal("34.40"), result);
        assertEquals(2, result.scale());
    }

    @Test
    @DisplayName("Deve calcular com valor zero")
    void deveCalcularComValorZero() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.VIDA);
        BigDecimal basePrice = BigDecimal.ZERO;

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        assertEquals(new BigDecimal("0.00"), result);
    }

    @Test
    @DisplayName("Deve manter precisão com valores grandes")
    void deveManterPrecisaoValoresGrandes() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.AUTO);
        BigDecimal basePrice = new BigDecimal("9999999.99");

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        // 9999999.99 + (9999999.99 × 0.055) + (9999999.99 × 0.04) + (9999999.99 × 0.01)
        // = 9999999.99 + 549999.99945 + 399999.9996 + 99999.9999
        assertEquals(new BigDecimal("11049999.99"), result);
    }

    @Test
    @DisplayName("Deve manter escala de 2 casas decimais no resultado")
    void deveManterEscalaDuasCasasDecimais() {
        // Arrange
        DefaultTaxPolicy policy = new DefaultTaxPolicy(Categoria.AUTO);
        BigDecimal basePrice = new BigDecimal("10.5");

        // Act
        BigDecimal result = policy.calculate(basePrice);

        // Assert
        assertEquals(2, result.scale());
        assertEquals(RoundingMode.HALF_UP, RoundingMode.HALF_UP); // Validação do modo de arredondamento
    }

}