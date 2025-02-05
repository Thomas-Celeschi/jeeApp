package myapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestICalculator {

    @Autowired
    ICalculator calculator;

    @Autowired
    @Qualifier("calculatorWithoutLog")
    ICalculator calculatorWithoutLog;

    @Autowired
    @Qualifier("decoratedCalculator")
    ICalculator decoratedCalculator;

    @Test
    public void testCalculator() {
        var res = calculator.add(10, 20);
        assertEquals(30, res);
        assertTrue(calculator instanceof CalculatorWithLog);
    }

    @Test
    public void testCalculatorWithoutLog() {
        var res = calculatorWithoutLog.add(10, 20);
        assertEquals(30, res);
        assertTrue(calculatorWithoutLog instanceof CalculatorWithLog);
    }

    @Test
    public void testDecoratedCalculator() {
        var res = decoratedCalculator.add(10, 20);
        assertEquals(30, res);
        assertTrue(decoratedCalculator instanceof DecoratedCalculator);
    }
}
