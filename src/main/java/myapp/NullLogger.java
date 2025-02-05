package myapp;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("nullLogger")
public class NullLogger implements ILogger {
}
