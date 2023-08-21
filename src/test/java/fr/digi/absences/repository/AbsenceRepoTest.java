package fr.digi.absences.repository;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Departement;
import fr.digi.absences.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;


/*
1. Créer un contexte Spring
2. Initialiser les beans repositories (Bean AbsenceRepo créé)
3. Appliquer un contexte transactionnel à chaque méthode (@Transactional)
=> rollback à la fin de chaque méthode
*/
@DataJpaTest
@ExtendWith(SpringExtension.class)
@Slf4j
class AbsenceRepoTest {

    @Autowired
    TestEntityManager testEntityManager;

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

        this.departement.setId(1L);
        this.departement.setLibelle("Hérault 34");
        this.departement.setEmployees(new ArrayList<>());
        this.departement.getEmployees().add(this.employee);

        this.absence.setId(1L);
        this.absence.setDateDebut(dateDebut);
        this.absence.setDateFin(dateFin);
        this.absence.setMotif("Congé Juilletiste");
        this.absence.setEmployee(employee);
        this.absence.setTypeConge(TypeConge.SANS_SOLDE);
        this.absence.setStatus(StatutAbsence.ATTENTE_VALIDATION);

        absences.add(this.absence);


        Mockito.when(this.employeeRepo.getReferenceById(1L)).thenReturn(this.employee);
        Mockito.when(this.absenceRepo.getReferenceById(1L)).thenReturn(this.absence);
    }

    @Test
    @Sql("mon-jeu-de-donnees.sql")
    void getAbsenceRepoTest() {
        // Hypothese
        String motif = "Congé Juilletiste";

        // Execution code
//        Mockito.when(this.absenceRepo.findByMotif(motif)).thenReturn(this.absence);

        // Vérification du résultat
        log.info(this.absenceRepo.getReferenceById(1L).getMotif());
//        Assertions.assertThat(this.absenceRepo.getReferenceById(1L).getMotif()).isEqualTo(motif);

    }

    @Test
    void createAbsenceRepoTest() {

    }

    @Test
    void updateAbsenceRepoTest() {

    }

    @Test
    void deleteAbsenceRepoTest() throws Exception {

    }
}