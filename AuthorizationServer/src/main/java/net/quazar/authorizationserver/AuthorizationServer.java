package net.quazar.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuthorizationServer {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServer.class, args);
    }

}
