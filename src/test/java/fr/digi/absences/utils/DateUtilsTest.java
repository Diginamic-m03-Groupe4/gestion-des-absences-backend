package fr.digi.absences.utils;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.digi.absences.utils.DateUtils.isAbsenceExist;
import static fr.digi.absences.utils.DateUtils.isBusinessDay;

@ExtendWith(SpringExtension.class)
@Slf4j
class DateUtilsTest {

    @Mock
    private AbsenceRepo absenceRepo;

    @Mock
    private EmployeeRepo employeeRepo;

    @BeforeEach
    void setUp() {

        LocalDate dateDebut = LocalDate.of(2023, 7, 5);
        LocalDate dateFin = LocalDate.of(2023, 7, 25);

        LocalDate dateDebut2 = LocalDate.of(2023, 7, 26);
        LocalDate dateFin2 = LocalDate.of(2023, 8, 15);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setNom("Toto");
        employee.setPrenom("Latete");
        employee.setEmail("toto@gmail.com");
        employee.setRole(Roles.EMPLOYEE);
        employee.setAbsences(new ArrayList<>());
        employee.setAbsenceRejetees(new ArrayList<>());
        List<Absence> absences = employee.getAbsences();


        int nbCongesRestants = 20;

        Absence absence = new Absence();
        absence.setId(1L);
        absence.setDateDebut(dateDebut);
        absence.setDateFin(dateFin);
        absence.setMotif("Congé Juilletiste");
        absence.setEmployee(employee);
        absence.setTypeConge(TypeConge.SANS_SOLDE);
        absence.setStatus(StatutAbsence.ATTENTE_VALIDATION);

        absences.add(absence);


        Absence absence2 = new Absence();
        absence2.setId(2L);
        absence2.setDateDebut(dateDebut2);
        absence2.setDateFin(dateFin2);
        absence2.setMotif("Congé Julliet-Aout");
        absence2.setEmployee(employee);
        absence2.setTypeConge(TypeConge.SANS_SOLDE);
        absence2.setStatus(StatutAbsence.ATTENTE_VALIDATION);

        Mockito.when(employeeRepo.getReferenceById(1L)).thenReturn(employee);

        Mockito.when(absenceRepo.getReferenceById(1L)).thenReturn(absence);
        Mockito.when(absenceRepo.getReferenceById(2L)).thenReturn(absence2);


//        log.info("Liste des absences : {}", absences.size());

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
    void should_ReturnTrue_When_isValidAbsenceIsCalled() {
    }

    @Test
    void should_ReturnTrue_When_AbsenceExistIsCalled() {
        // Hypothèse
        Employee employee2 = employeeRepo.getReferenceById(1L);
        Absence absence2 = absenceRepo.getReferenceById(2L);
        List<Absence> absences = employee2.getAbsences();

        // Execution du code à tester
        boolean absenceExist = isAbsenceExist(absences, absence2.getDateDebut(), absence2.getDateFin());

        // verification du résultat
        Assertions.assertThat(absenceExist).isTrue();

    }

    @Test
    void should_ReturnTrue_When_isBusinesDayIsCalledWithWeekDay(){

        // hypothèse
        Absence absenceFound = absenceRepo.getReferenceById(1L);
        // exécution du code à tester
        boolean businessDay = isBusinessDay(absenceFound.getDateDebut());
        // vérification du résultat
        Assertions.assertThat(businessDay).isTrue();

    }

}