package fr.digi.absences.service;

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
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

/**
 * Classe contenant la logique du processus de validation des absences
 * s'exécutant chaque jour à 23h
 * */
@Service
@RequiredArgsConstructor
@Slf4j
public class TraitementNuit {

    /**Clé de l'API vers Brevo pour envoyer des mails*/
    private String API_KEY = "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";
    /**URL de l'API Brevo pour envoyer des mails*/
    private String URL_SMTP = "https://api.brevo.com/v3/emailCampaigns";
    /**Nom du paramètre du nom de l'employé dans le template du mail*/
    private String EMPLOYEE_PARAM = "{%employee_name%}";
    /**Nom du paramètre du nom de l'entreprise dans le template du mail*/
    private String COMPANY_PARAM = "{%company_name%}";
    private final AbsenceRepo absenceRepo;
    private final EmployeeRepo employeeRepo;
    /**Nom de l'entreprise, récupéré dans application.properties*/
    @Value("${company.name}")
    private String COMPANY_NAME;
    /**Mail de l'entreprise, récupérée dans application.properties*/
    @Value("${company.email}")
    private String COMPANY_MAIL;
    //@Scheduled(cron = "0 23 * * * *")
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void executerTraitementNuit(){
        List<Absence> absencesDuJour = absenceRepo.findByDateDemandeAndStatus(LocalDate.now(), StatutAbsence.INITIALE);
        Map<Employee, List<Absence>> absencesEmployee = new HashMap<>();
        for (Absence absence : absencesDuJour) {
            if(!absencesEmployee.containsKey(absence.getEmployee())){
                absencesEmployee.put(absence.getEmployee(), new ArrayList<>());
            }
        }
        log.info(absencesEmployee.toString());
        for(Employee employee : absencesEmployee.keySet()){
            LocalDate dateDebut = DateUtils.findDateDebutAnneeAbsence(employee);
            absenceRepo.findByDateDebutBetweenAndEmployee(dateDebut, dateDebut.plusYears(1), employee);
            if(absencesEmployee.get(employee).size() > employee.getNombresJoursRestantsCPSansNonValides()){
                absencesEmployee.get(employee).forEach(absence -> absence.setStatus(StatutAbsence.REJETEE));
            } else {
                absencesEmployee.get(employee).forEach(absence -> absence.setStatus(StatutAbsence.ATTENTE_VALIDATION));
            }
            absenceRepo.saveAll(absencesEmployee.get(employee));
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
                .name("Absences")
                .htmlContent(TemplateUtils.formatString(parameters, template))
                .sender(MailPerson.builder().name(COMPANY_NAME).email(COMPANY_MAIL).build())
                .subject("Demande d'absence d'un employé")
                .build();
        log.info(mailBody.toString());
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
            log.info(request.headers().toString());
            log.info(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        } catch (IOException | InterruptedException e) {
            throw new TechnicalException(e.getMessage());
        }
    }

    private String getTemplate(){
        File file;
        try {
            file = ResourceUtils.getFile("classpath:mail.html");
            return String.join("", Files.readAllLines(file.toPath()));
        } catch (IOException e) {
            throw new TechnicalException(e.getMessage());
        }
    }
}
