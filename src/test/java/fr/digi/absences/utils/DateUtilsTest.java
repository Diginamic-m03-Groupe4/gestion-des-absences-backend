package fr.digi.absences.utils;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(DateUtils.class)
@AllArgsConstructor
class DateUtilsTest {

    EmployeeRepo employeeRepo;
    AbsenceRepo absenceRepo;

    @BeforeEach
    void setUp() {
        LocalDate dateDebut = LocalDate.of(2023, 5, 5);
        LocalDate dateFin = LocalDate.of(2023, 5, 25);
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNom("Toto");
        employee.setPrenom("Latete");
        employee.setEmail("toto@gmail.com");
        employee.setRole(Roles.EMPLOYEE);
        employee.setAbsenceRejetees(new ArrayList<>());
        employee.setAbsences(new ArrayList<>());
        List<Absence> absences = employee.getAbsences();

        int nbCongesRestants = 20;
        TypeConge typeConge = TypeConge.SANS_SOLDE;
        StatutAbsence statutAbsence = StatutAbsence.INITIALE;
        Map<Integer, JourFerie> jourFerieHashMap = new HashMap<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNbJoursEntreDeuxJours() {
    }

    @Test
    void getNbJoursRestants() {
    }

    @Test
    void testGetNbJoursRestants() {
    }

    @Test
    void isValidAbsence() {
    }

    @Test
    void isAbsenceExist() {
    }
}