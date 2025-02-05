package myapp;

import java.util.Date;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("stderrLogger")
@Primary
@Qualifier("stderr")
@Profile("devel")
public class StderrLogger implements ILogger {

    @PostConstruct
    public void start() {
        System.err.printf("Start %s\n", this);
    }

    @PreDestroy
    public void stop() {
        System.err.printf("Stop %s\n", this);
    }

    @Override
    public void log(String message) {
        System.err.printf("%tF %1$tR | %s\n", new Date(), message);
    }

}