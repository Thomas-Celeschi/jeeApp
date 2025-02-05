package myapp;

import org.springframework.stereotype.Service;

import lombok.Data;

@Service("calculator")
@Data
public class CalculatorImpl implements ICalculator{
    @Override
    public int add(int a, int b) {
        return (a + b);
    }
}
