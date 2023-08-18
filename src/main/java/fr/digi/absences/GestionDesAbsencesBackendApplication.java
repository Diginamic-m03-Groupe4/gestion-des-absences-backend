package fr.digi.absences;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestionDesAbsencesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionDesAbsencesBackendApplication.class, args);
    }

}
