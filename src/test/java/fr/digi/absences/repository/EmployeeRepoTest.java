package fr.digi.absences.repository;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Departement;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@Slf4j
@WebMvcTest(ConfigurableApplicationContext.class)
class EmployeeRepoTest {

    @Container
    private static MariaDBContainer container = new MariaDBContainer(DockerImageName.parse("mariadb:latest"));

    @DynamicPropertySource
    static void setUpProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @MockBean
    AbsenceRepo absenceRepo;

    @MockBean
    Employee employee;

    @MockBean
    Departement departement;

    @MockBean
    Absence absence;

    @MockBean
    EmployeeRepo employeeRepo;

    @MockBean
    DepartementRepo departementRepo;

    @MockBean
    JwtService jwtService;

    @BeforeAll
    static void startContainer() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        LocalDate dateDebut = LocalDate.of(2023, 7, 17);
        LocalDate dateFin = LocalDate.of(2023, 7, 25);

        employee = new Employee();
        employee.setId(1L);
        employee.setNom("Toto");
        employee.setPrenom("Latete");
        employee.setEmail("toto@gmail.com");
        employee.setRole(Roles.EMPLOYEE);
        employee.setAbsences(new ArrayList<>());
        employee.setAbsenceRejetees(new ArrayList<>());
        List<Absence> absences = employee.getAbsences();

        departement = new Departement();
        departement.setId(1L);
        departement.setLibelle("Hérault 34");
        departement.setEmployees(new ArrayList<>());
        departement.getEmployees().add(employee);

        absence = new Absence();
        absence.setId(1L);
        absence.setDateDebut(dateDebut);
        absence.setDateFin(dateFin);
        absence.setMotif("Congé Juilletiste");
        absence.setEmployee(employee);
        absence.setTypeConge(TypeConge.SANS_SOLDE);
        absence.setStatus(StatutAbsence.ATTENTE_VALIDATION);

        absences.add(absence);

    }


    @Test
    void findManagers() {
    }

    @Test
    void findByEmail() {
    }

    @AfterAll
    static void stopContainer() {
        container.stop();
    }
}