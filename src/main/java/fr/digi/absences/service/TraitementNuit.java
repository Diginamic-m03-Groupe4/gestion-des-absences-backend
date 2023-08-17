package fr.digi.absences.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.exception.TechnicalException;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.service.types.MailBody;
import fr.digi.absences.service.types.MailPerson;
import fr.digi.absences.utils.DateUtils;
import fr.digi.absences.utils.TemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraitementNuit {
    private final String API_KEY = "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";
    private final String URL_SMTP = "https://api.sendinblue.com/v3/smtp/email";
    private static final String EMPLOYEE_PARAM = "{%employee_name%}";
    private static final String COMPANY_PARAM = "{%company_name%}";
    private final AbsenceRepo absenceRepo;
    private final EmployeeRepo employeeRepo;
    @Value("${company.name}")
    private final String COMPANY_NAME;
    @Value("${company.email}")
    private final String COMPANY_MAIL;
    @Scheduled(cron = "0 23 * * * *")
    //@Scheduled(fixedDelay = 1000)
    public void executerTraitementNuit(){
        List<Absence> absencesDuJour = absenceRepo.findByDateDemandeAndStatus(LocalDate.now(), StatutAbsence.INITIALE);
        Map<Employee, List<Absence>> absencesEmployee = new HashMap<>();
        for (Absence absence : absencesDuJour) {
            if(!absencesEmployee.containsKey(absence.getEmployee())){
                absencesEmployee.put(absence.getEmployee(), new ArrayList<>());
            }
        }
        for(Employee employee : absencesEmployee.keySet()){
            LocalDate dateDebut = DateUtils.findDateDebutAnneeAbsence(absencesEmployee.get(employee).get(0));
            absenceRepo.findByDateDebutBetweenAndEmployee(dateDebut, dateDebut.plusYears(1), employee);
            if(absencesEmployee.get(employee).size() > employee.getNombresJoursRestantsCPSansNonValides()){
                absencesEmployee.get(employee).forEach(absence -> absence.setStatus(StatutAbsence.REJETEE));
            } else {
                absencesEmployee.get(employee).forEach(absence -> absence.setStatus(StatutAbsence.ATTENTE_VALIDATION));
            }
            sendOneMail(employee);
        }
    }

    private void sendOneMail(Employee employee) {
        List<MailPerson> tos = employeeRepo.findManagers(employee.getDepartement(), Roles.MANAGER).stream()
                .map(employee1 -> MailPerson.builder().name(employee1.getFullName()).email(employee1.getEmail()).build())
                .toList();
        MailPerson sender = MailPerson.builder()
                .email(COMPANY_MAIL)
                .name(COMPANY_NAME)
                .build();
        String template = getTemplate();
        Map<String, String> parameters = new HashMap<>();
        parameters.put(COMPANY_PARAM, COMPANY_NAME);
        parameters.put(EMPLOYEE_PARAM, employee.getFullName());
        MailBody mailBody = MailBody.builder()
                .to(tos)
                .htmlContent(TemplateUtils.formatString(parameters, template))
                .sender(MailPerson.builder().name(COMPANY_NAME).email(COMPANY_MAIL).build())
                .subject("Demande d'absence d'un employé")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_SMTP))
                    .header("accept", "application/json")
                    .header("api-key", API_KEY)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(mailBody)))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new TechnicalException(e.getMessage());
        }
    }

    private String getTemplate(){
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:mail.html");
        } catch (FileNotFoundException e) {
            throw new TechnicalException(e.getMessage());
        }
        return file.toString();
    }
}
