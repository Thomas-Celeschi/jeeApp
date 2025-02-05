package myapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Hello service
 */
@Service("helloService")
public class HelloService implements IHello {

	@Value("${helloMessage}")
	private String helloMessage = "";

	@Override
	public void hello() {
		System.err.printf("%s\n", helloMessage);
	}

	/* méthode facultative d'initialisation */
	@PostConstruct
	public void start() {
		System.err.printf("start %s\n", this);
	}

	/* méthode facultative de fermeture */
	@PreDestroy
	public void stop() {
		System.err.printf("stop %s\n", this);
	}

}

