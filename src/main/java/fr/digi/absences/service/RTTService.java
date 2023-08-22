package fr.digi.absences.service;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.StatutAbsenceEmployeur;
import fr.digi.absences.dto.RTTEmployeurDTO;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.RTTEmployeur;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.mapper.AbsenceMap;
import fr.digi.absences.mapper.RTTEmployeurMap;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.repository.RTTEmployeurRepo;
import fr.digi.absences.utils.DateUtils;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class RTTService {

    private RTTEmployeurRepo rttEmployeurRepo;

    private RTTEmployeurMap rttEmployeurMap;

    private EmployeeRepo employeeRepo;

    private JourFeriesService jourFeriesService;

    private AbsenceSrvc absenceSrvc;
    private AbsenceRepo absenceRepo;
    private AbsenceMap absenceMap;


    private int getNbRttRestants() {
        return 0;
    }

    /**
     * @return
     */
    public List<RTTEmployeur> getRTTEmployeur() {
        return rttEmployeurRepo.findAll();
    }

    /**
     * @param id
     * @return
     */
    public RTTEmployeurDTO getRTTEmployeurByID(Long id) {
        RTTEmployeur rttEmployeur = rttEmployeurRepo.findById(id).orElseThrow(EntityExistsException::new);
        return rttEmployeurMap.toRTTEmployeurDTO(rttEmployeur);
    }

    /**
     * @param rttEmployeurDTO
     * @return
     */
    public RTTEmployeur createRTT(RTTEmployeurDTO rttEmployeurDTO) {
        applyCreationLogic(rttEmployeurDTO);

        List<Absence> absenceMatchRttEmployeur = absenceRepo.findAbsenceMatchRttEmployeur(rttEmployeurDTO.getDate());
        if (!absenceMatchRttEmployeur.isEmpty()) {
            Iterator<Absence> iter = absenceMatchRttEmployeur.listIterator();
            while (iter.hasNext()) {
                Absence absence = iter.next();
                absence.setStatus(StatutAbsence.REJETEE);
                absenceRepo.save(absence);
            }
        }

        RTTEmployeur rttEmployeur = rttEmployeurMap.toRTTEmployeur(rttEmployeurDTO);
        return this.rttEmployeurRepo.save(rttEmployeur);
    }

    /**
     * @param rttEmployeurDTO
     * @param id
     */
    public void updateRTT(RTTEmployeurDTO rttEmployeurDTO, Long id) {
        applyCreationLogic(rttEmployeurDTO);
        RTTEmployeur rttEmployeur = rttEmployeurRepo.findById(id).orElseThrow(EntityExistsException::new);
        rttEmployeurMap.modifyRTTEmployeur(rttEmployeur, rttEmployeurDTO);
        rttEmployeurRepo.save(rttEmployeur);
    }

    /**
     * @param id
     */
    public void deleteRTT(Long id) {
        RTTEmployeur rttEmployeur = this.rttEmployeurRepo.findById(id).orElseThrow(EntityExistsException::new);
        if (!LocalDate.now().isBefore(rttEmployeur.getDate())) {
            throw new BrokenRuleException("Il n'est pas possible de supprimer un RTT employeur dans le passé");
        }
        rttEmployeurRepo.delete(rttEmployeur);
    }

    /**
     * @param rttEmployeurDTO
     */
    private void applyCreationLogic(RTTEmployeurDTO rttEmployeurDTO) {

        // TODO LOGIC METIER: Le statut de l'absence doit etre en INITIALE
        if (rttEmployeurDTO.getStatutAbsenceEmployeur().equals(StatutAbsenceEmployeur.VALIDEE)) {
            throw new BrokenRuleException("On ne peut pas modifier ou creer une journée de RTT employeur déjà validée");
        }

        // TODO LOGIC METIER: un RTT employeur doit etre dans le future
        if (!LocalDate.now().isBefore(rttEmployeurDTO.getDate())) {
            throw new BrokenRuleException(("La date du jour de RTT doit être dans le futur"));
        }

        // TODO LOGIC METIER; un RTT employeur ne peut pas etre déclaré sur un jour férié
        if (DateUtils.isRTTOnJourFerie(rttEmployeurDTO.getDate(), jourFeriesService.joursFeries(rttEmployeurDTO.getDate().getYear()))) {
            throw new BrokenRuleException("Le jour RTT posé ne peut pas être un jour férié.");
        }

        // TODO LOGIC METIER: un jour RTT doit etre un jour ouvré
        if (!DateUtils.isBusinessDay(rttEmployeurDTO.getDate())) {
            throw new BrokenRuleException("Il est interdit de saisir une RTT employeur un samedi ou un dimanche");
        }

        // TODO LOGIC METIER: Un RTT employeur ne peut pas etre déclaré sur un jour RTT existant
        if (rttEmployeurRepo.existsByDate(rttEmployeurDTO.getDate())) {
            throw new BrokenRuleException("Le jour RTT posé ne peut pas être un jour RTT existant");
        }
    }
}
