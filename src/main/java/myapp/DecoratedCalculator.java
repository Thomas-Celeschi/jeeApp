package myapp;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("decoCalculator")
@Data
public class DecoratedCalculator implements ICalculator {

    private ICalculator calculator = new CalculatorImpl();

    private ILogger logger = new NullLogger();

    @PostConstruct
    public void start() {
        Assert.notNull(logger, "no logger");
        Assert.notNull(calculator, "no calculator");
    }

    @Override
    public int add(int a, int b) {
        logger.log(String.format("add(%d,%d)", a, b));
        return calculator.add(a, b);
    }
}
