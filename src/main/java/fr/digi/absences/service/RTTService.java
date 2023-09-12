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
    public List<RTTEmployeur> getRTTEmployeur(int annee) {
        return rttEmployeurRepo.findByAnnee(annee);
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
     * @param dtos
     * @return
     */
    public List<RTTEmployeurDTO> createRTTs( List<RTTEmployeurDTO> dtos) {
        dtos.forEach(this::applyCreationLogic);
        return dtos.stream()
                .map(rttEmployeurDTO -> {
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
                    return this.rttEmployeurMap.toRTTEmployeurDTO(this.rttEmployeurRepo.save(rttEmployeur));
                })
                .toList();
    }

    /**
     * @param rttEmployeurDTO
     */
    public RTTEmployeurDTO updateRTT(RTTEmployeurDTO rttEmployeurDTO) {
        RTTEmployeur rttEmployeur = rttEmployeurRepo.findById(rttEmployeurDTO.getId()).orElseThrow(EntityExistsException::new);
        applyModificationLogic(rttEmployeurDTO, rttEmployeur);
        rttEmployeurMap.modifyRTTEmployeur(rttEmployeur, rttEmployeurDTO);
        return rttEmployeurMap.toRTTEmployeurDTO(rttEmployeurRepo.save(rttEmployeur));
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

    private void applyCreationLogic(RTTEmployeurDTO dto){
        if (rttEmployeurRepo.existsByDate(dto.getDate())) {
            throw new BrokenRuleException("Le jour RTT posé ne peut pas être un jour RTT existant");
        }
        applyCommonLogic(dto);
    }
    private void applyCommonLogic(RTTEmployeurDTO dto){
        if (LocalDate.now().isAfter(dto.getDate())) {
            throw new BrokenRuleException(("La date du jour de RTT doit être dans le futur"));
        }
        if (DateUtils.isRTTOnJourFerie(dto.getDate(), jourFeriesService.joursFeries(dto.getDate().getYear()))) {
            throw new BrokenRuleException("Le jour RTT posé ne peut pas être un jour férié.");
        }
        if (!DateUtils.isBusinessDay(dto.getDate())) {
            throw new BrokenRuleException("Il est interdit de saisir une RTT employeur un samedi ou un dimanche");
        }
    }

    /**
     * @param rttEmployeurDTO
     */
    private void applyModificationLogic(RTTEmployeurDTO rttEmployeurDTO, RTTEmployeur rttEmployeur) {
        if (rttEmployeur.getStatutAbsenceEmployeur().equals(StatutAbsenceEmployeur.VALIDEE)) {
            throw new BrokenRuleException("On ne peut pas modifier ou creer une journée de RTT employeur déjà validée");
        }
        if (rttEmployeurRepo.existsByDate(rttEmployeurDTO.getDate(), rttEmployeur.getId()) > 0) {
            throw new BrokenRuleException("Le jour RTT posé ne peut pas être un jour RTT existant");
        }
        applyCommonLogic(rttEmployeurDTO);
    }
}
