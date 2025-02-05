package myapp;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.Data;

@Service("calculatorWithLog")
@Primary
@Data
public class CalculatorWithLog implements ICalculator {

    @Autowired
    private ILogger logger;

    @PostConstruct
    public void start() {
        Assert.notNull(logger, "no logger");
    }

    @Override
    public int add(int a, int b) {
        logger.log(String.format("add(%d,%d)", a, b));
        return (a + b);
    }

}