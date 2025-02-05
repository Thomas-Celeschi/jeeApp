package myapp;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import java.util.Date;

@Service("stdoutLogger")
@Primary
@Qualifier("stdout")
@Profile("!devel") // classe pour tous les profils sauf devel
public class StdoutLogger implements ILogger {

        @PostConstruct
        public void start() {
                System.out.printf("Start %s\n", this);
        }

        @PreDestroy
        public void stop() {
                System.out.printf("Stop %s\n", this);
        }

        @Override
        public void log(String message) {
                System.out.printf("%tF %1$tR | %s\n", new Date(), message);
        }

}