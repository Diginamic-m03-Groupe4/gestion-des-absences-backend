package fr.digi.absences.service;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@Slf4j
class AbsenceSrvcTest {

    @Mock
    Absence absence;

    @MockBean
    AbsenceSrvc absenceSrvc;

    @MockBean
    Employee employee;

    @MockBean
    EmployeeRepo employeeRepo;

    @MockBean
    AbsenceRepo absenceRepo;

    @MockBean
    AbsenceDto absenceDto;

    @BeforeEach
    void setUp() {

        LocalDate dateDebut = LocalDate.of(2023, 7, 17);
        LocalDate dateFin = LocalDate.of(2023, 7, 25);

        this.employee.setId(1L);
        this.employee.setNom("Toto");
        this.employee.setPrenom("Latete");
        this.employee.setEmail("toto@gmail.com");
        this.employee.setRole(Roles.EMPLOYEE);
        this.employee.setAbsences(new ArrayList<>());
        this.employee.setAbsenceRejetees(new ArrayList<>());
        List<Absence> absences = this.employee.getAbsences();


        this.absence.setId(1L);
        this.absence.setDateDebut(dateDebut);
        this.absence.setDateFin(dateFin);
        this.absence.setMotif("Congé Juilletiste");
        this.absence.setEmployee(employee);
        this.absence.setTypeConge(TypeConge.SANS_SOLDE);
        this.absence.setStatus(StatutAbsence.ATTENTE_VALIDATION);

        absences.add(this.absence);

        this.employeeRepo.save(this.employee);

        this.absenceRepo.save(this.absence);


        Mockito.when(this.employeeRepo.getReferenceById(1L)).thenReturn(this.employee);
        Mockito.when(this.absenceRepo.getReferenceById(1L)).thenReturn(this.absence);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAbsenceTest() {

    }

    @Test
    void createAbsenceTest() {

    }

    @Test
    void updateAbsenceTest() {

    }

    @Test
    void deleteAbsenceTest() {

    }

    @Test
    void applyModificationLogicTest() {}

    @Test
    void applyCreationLogicTest() {}

}