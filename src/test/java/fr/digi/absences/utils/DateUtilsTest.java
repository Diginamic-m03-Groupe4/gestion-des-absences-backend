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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.*;

import static fr.digi.absences.utils.DateUtils.*;

@ExtendWith(SpringExtension.class)
@Slf4j
class DateUtilsTest {

    @Mock
    private AbsenceRepo absenceRepo;
    @Mock
    private EmployeeRepo employeeRepo;

    @MockBean
    Employee employee;

    @BeforeEach
    void setUp() {

        int nbCongesRestants = 20;

        LocalDate dateDebut = LocalDate.of(2023, 7, 17);
        LocalDate dateFin = LocalDate.of(2023, 7, 25);

        LocalDate dateDebut2 = LocalDate.of(2023, 7, 26);
        LocalDate dateFin2 = LocalDate.of(2023, 8, 14);

        this.employee.setId(1L);
        this.employee.setNom("Toto");
        this.employee.setPrenom("Latete");
        this.employee.setEmail("toto@gmail.com");
        this.employee.setRole(Roles.EMPLOYEE);
        this.employee.setAbsences(new ArrayList<>());
        this.employee.setAbsenceRejetees(new ArrayList<>());
        List<Absence> absences = this.employee.getAbsences();


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

        Mockito.when(this.employeeRepo.getReferenceById(1L)).thenReturn(this.employee);

        Mockito.when(this.absenceRepo.getReferenceById(1L)).thenReturn(absence);
        Mockito.when(this.absenceRepo.getReferenceById(2L)).thenReturn(absence2);

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
        //Hypothese
        Absence absence = absenceRepo.getReferenceById(1L);
        // Execution du code
        boolean validAbsence1 = isValidAbsence(absence.getDateDebut(), absence.getDateFin(), JoursOuvresFrance.joursFeries(2023));
        // Verification resultat
        Assertions.assertThat(validAbsence1).isTrue();

        Absence absence2 = absenceRepo.getReferenceById(2L);
        boolean validAbsence2 = isValidAbsence(absence2.getDateDebut(), absence2.getDateFin(), JoursOuvresFrance.joursFeries(2023));
        Assertions.assertThat(validAbsence2).isTrue();


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