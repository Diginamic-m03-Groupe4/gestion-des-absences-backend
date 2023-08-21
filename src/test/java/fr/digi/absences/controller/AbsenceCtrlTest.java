package fr.digi.absences.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.service.AbsenceSrvc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

import org.mockito.InjectMocks;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AbsenceCtrl.class)
@ExtendWith(SpringExtension.class)
@Slf4j
class AbsenceCtrlTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AbsenceSrvc absenceSrvc;

    @MockBean
    Employee employee;

    @MockBean
    Absence absence;

    @MockBean
    AbsenceRepo absenceRepo;

    @MockBean
    EmployeeRepo employeeRepo;

    @BeforeEach
    void setuo(){

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

        Mockito.when(this.employeeRepo.getReferenceById(1L)).thenReturn(this.employee);
        Mockito.when(this.absenceRepo.getReferenceById(1L)).thenReturn(this.absence);
    }

    @Test
    void createAbsenceTest() throws Exception {
        Absence absenceToTest = this.absence;
        mvc.perform(MockMvcRequestBuilders.post("absence")
                .content(asJsonString(absenceToTest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateDebut").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateFind").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.motf").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.typeConge").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(""));
    }

    @Test
    void updateAbsenceTest() throws Exception {

    }

    @Test
    void deleteAbsenceTest() throws Exception {

    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}