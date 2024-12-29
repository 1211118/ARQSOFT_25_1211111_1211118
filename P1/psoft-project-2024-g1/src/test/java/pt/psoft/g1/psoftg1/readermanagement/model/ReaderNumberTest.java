package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class ReaderNumberTest {

    private int currentYear;

    @BeforeEach
    public void setup() {
        // Obtém o ano atual para os testes
        currentYear = LocalDate.now().getYear();
    }

    @Test
    public void testConstructorWithYearAndNumber() {
        // Teste com ano específico e número específico
        int year = 2021;
        int number = 12345;
        ReaderNumber readerNumber = new ReaderNumber(year, number);

        // Verifica o formato esperado "2021/12345"
        assertEquals("2021/12345", readerNumber.toString());
    }

    @Test
    public void testConstructorWithOnlyNumber() {
        // Teste com número e ano atual
        int number = 67890;
        ReaderNumber readerNumber = new ReaderNumber(number);

        // Verifica se o valor está no formato "ano_atual/number"
        assertEquals(currentYear + "/67890", readerNumber.toString());
    }

    @Test
    public void testConstructorWithDifferentYearsAndNumbers() {
        // Teste com diferentes anos e números para garantir a formatação correta

        // Caso: ano mais recente e número pequeno
        ReaderNumber readerNumber1 = new ReaderNumber(2023, 1);
        assertEquals("2023/1", readerNumber1.toString());

        // Caso: ano passado e número grande
        ReaderNumber readerNumber2 = new ReaderNumber(2020, 99999);
        assertEquals("2020/99999", readerNumber2.toString());

        // Caso: ano no futuro e número intermediário
        ReaderNumber readerNumber3 = new ReaderNumber(2025, 123);
        assertEquals("2025/123", readerNumber3.toString());
    }

    @Test
    public void testConstructorWithOnlyNumberDifferentCases() {
        // Teste com diferentes números usando o ano atual

        // Caso: número pequeno
        ReaderNumber readerNumber1 = new ReaderNumber(1);
        assertEquals(currentYear + "/1", readerNumber1.toString());

        // Caso: número intermediário
        ReaderNumber readerNumber2 = new ReaderNumber(100);
        assertEquals(currentYear + "/100", readerNumber2.toString());

        // Caso: número grande
        ReaderNumber readerNumber3 = new ReaderNumber(99999);
        assertEquals(currentYear + "/99999", readerNumber3.toString());
    }

    @Test
    public void testToString() {
        // Teste do método toString diretamente

        // Caso: com ano específico e número específico
        ReaderNumber readerNumber1 = new ReaderNumber(2022, 54321);
        assertEquals("2022/54321", readerNumber1.toString());

        // Caso: somente número com ano atual
        ReaderNumber readerNumber2 = new ReaderNumber(9876);
        assertEquals(currentYear + "/9876", readerNumber2.toString());
    }
}
