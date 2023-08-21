package fr.digi.absences.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.digi.absences.consts.*;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.entity.RTTEmployeur;
import fr.digi.absences.exception.TechnicalException;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.repository.RTTEmployeurRepo;
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
    private final RTTEmployeurRepo rttEmployeurRepo;
    private final EmployeeRepo employeeRepo;
    /**Nom de l'entreprise, récupéré dans application.properties*/
    @Value("${company.name}")
    private String COMPANY_NAME;
    /**Mail de l'entreprise, récupérée dans application.properties*/
    @Value("${company.email}")
    private String COMPANY_MAIL;
    /**
     * Méthode s'exécutant tous les jours à 23h
     * - Prend les absences du jour
     * - Les lies à un employé
     * - Récupère toutes ses absences de l'année en cours
     * - Vérifie la taille de la liste
     * - Si la taille de la liste est plus grande que son nombre d'absences autorisées, l'absence
     * passe en rejetée
     * - Sinon, elles passent en attente de validation
     * - Un mail est envoyé aux managers du département des employés concernés via une autre méthode
     * */
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
            absencesEmployee.get(absence.getEmployee()).add(absence);
        }

        List<RTTEmployeur> rttsEmployeurAjout = rttEmployeurRepo.findByStatutAbsenceEmployeur(StatutAbsenceEmployeur.INITIALE);
        List<RTTEmployeur> rttsEmployeursSurAnnee = rttEmployeurRepo.findByAnnee(LocalDate.now().getYear());
        if(rttsEmployeursSurAnnee.size() > Days.NB_RTT_EMPLOYEUR){
            rttsEmployeurAjout.forEach(rttEmployeur -> rttEmployeur.setStatutAbsenceEmployeur(StatutAbsenceEmployeur.REJETEE));
        } else {
            rttsEmployeurAjout.forEach(rttEmployeur -> rttEmployeur.setStatutAbsenceEmployeur(StatutAbsenceEmployeur.VALIDEE));
        }

        for(Employee employee : absencesEmployee.keySet()){

            LocalDate dateDebut = DateUtils.findDateDebutAnneeAbsence(employee);
            List<Absence> absences = absenceRepo.findByDateDebutBetweenAndEmployee(dateDebut, dateDebut.plusYears(1), employee);
            List<Absence> congePayes = absences.stream().filter(absence -> absence.getTypeConge().equals(TypeConge.PAYE)).toList();
            List<Absence> rttsEmployes = absences.stream().filter(absence -> absence.getTypeConge().equals(TypeConge.RTT_EMPLOYE)).toList();
            absences = absences.stream().filter(absence -> !(absence.getTypeConge().equals(TypeConge.RTT_EMPLOYE) || absence.getTypeConge().equals(TypeConge.PAYE))).toList();

            if(congePayes.size() > employee.getNombreJoursRestantsCongesPayes()){
                absencesEmployee.get(employee).stream()
                        .filter(absence -> absence.getTypeConge().equals(TypeConge.PAYE))
                        .forEach(absence -> absence.setStatus(StatutAbsence.REJETEE));
            } else {
                absencesEmployee.get(employee).stream()
                        .filter(absence -> absence.getTypeConge().equals(TypeConge.PAYE))
                        .forEach(absence -> absence.setStatus(StatutAbsence.ATTENTE_VALIDATION));
            }
            if(rttsEmployes.size() > employee.getNombresJoursRestantsRTT()){
                absencesEmployee.get(employee).stream()
                        .filter(absence -> absence.getTypeConge().equals(TypeConge.RTT_EMPLOYE))
                        .forEach(absence -> absence.setStatus(StatutAbsence.REJETEE));
            } else {
                absencesEmployee.get(employee).stream()
                        .filter(absence -> absence.getTypeConge().equals(TypeConge.RTT_EMPLOYE))
                        .forEach(absence -> absence.setStatus(StatutAbsence.ATTENTE_VALIDATION));
            }

            absences.forEach(absence -> absence.setStatus(StatutAbsence.ATTENTE_VALIDATION));

            log.info(congePayes.toString());
            log.info(rttsEmployes.toString());
            log.info(absences.toString());

            absenceRepo.saveAll(congePayes);
            absenceRepo.saveAll(rttsEmployes);
            absenceRepo.saveAll(absences);

            sendOneMail(employee);
        }
    }

    /**
     * Envoie un email aux managers de l'employé lui informant qu'il a fait une ou plusieurs demandes
     * d'absence
     * Récupère une clé d'API, le mail d'envoie et le nom de celui qui envoie dans application.properties
     * Récupère un template de mail paramétrable, et utilise TemplateUtils pour valoriser les paramètes.
     * Envoie ensuite le mail en utilisant l'API de brevo
     * */
    private void sendOneMail(Employee employee) {
        List<MailPerson> tos = employeeRepo.findManagers(employee.getDepartement(), Roles.MANAGER).stream()
                .map(employee1 -> MailPerson.builder().name(employee1.getFullName()).email(employee1.getEmail()).build())
                .toList();
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
            log.info(client.send(request, HttpResponse.BodyHandlers.ofString()).headers().toString());
            log.info(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        } catch (IOException | InterruptedException e) {
            throw new TechnicalException(e.getMessage());
        }
    }

    /**
     * Permet de récupérer le contenu d'un template dans les ressources
     * */
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
