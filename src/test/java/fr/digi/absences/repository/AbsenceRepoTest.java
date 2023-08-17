package fr.digi.absences.repository;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Departement;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import static org.mockito.BDDMockito.*;


/*
1. Créer un contexte Spring
2. Initialiser les beans repositories (Bean AbsenceRepo créé)
3. Appliquer un contexte transactionnel à chaque méthode (@Transactional)
=> rollback à la fin de chaque méthode
*/

//@SpringBootTest
@Testcontainers
@Slf4j
@WebMvcTest(ConfigurableApplicationContext.class)
class AbsenceRepoTest {

    @Container
    private static MariaDBContainer container = new MariaDBContainer(DockerImageName.parse("mariadb:latest"));

    @DynamicPropertySource
    static void setUpProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }


    @BeforeAll
    static void startContainer() {
        container.start();
    }

    @Mock
    Absence absence;

    @MockBean
    AbsenceRepo absenceRepo;

    @MockBean
    Employee employee;

    @MockBean
    EmployeeRepo employeeRepo;

    @MockBean
    Departement departement;

    @MockBean
    DepartementRepo departementRepo;

    @MockBean
    JwtService jwtService;


    @BeforeEach
    void setUp() {
    }


    /**
     *
     */
    @Test
    void getAbsenceRepoTest() {
        // Hypothese

        // Execution code

        // Vérification du résultat

    }

    /**
     *
     */
    @Test
    void createAbsenceRepoTest() {

        // mise en place pour le test

        // Invocation de la méthode

        // Verification du resultat

    }

    /**
     *
     */
    @Test
    void updateAbsenceRepoTest() {

    }

    /**
     * @throws Exception
     */
    @Test
    void deleteAbsenceRepoTest() throws Exception {

    }

    @AfterAll
    static void stopContainer() {
        container.stop();
    }
}