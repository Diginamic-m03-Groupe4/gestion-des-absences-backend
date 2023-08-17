package fr.digi.absences.service;

import fr.digi.absences.consts.StatutAbsenceEmployeur;
import fr.digi.absences.dto.RTTEmployeurDTO;
import fr.digi.absences.entity.RTTEmployeur;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.mapper.RTTEmployeurMap;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.repository.RTTEmployeurRepo;
import fr.digi.absences.utils.DateUtils;
import fr.digi.absences.utils.JoursOuvresFrance;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Service
@AllArgsConstructor
public class RTTService {

    private RTTEmployeurRepo rttEmployeurRepo;

    private RTTEmployeurMap rttEmployeurMap;

    private EmployeeRepo employeeRepo;


    private int getNbRttRestants() {
        return 0;
    }

    public Collection<RTTEmployeurDTO> getRTTEmployeur(RTTEmployeurDTO rttEmployeurDTO) {

        Collection<RTTEmployeur> byDateAndStatutAbsenceEmployeur = rttEmployeurRepo.findByDateAndStatutAbsenceEmployeur(rttEmployeurDTO.getDate(), StatutAbsenceEmployeur.INITIALE);

        Iterator<RTTEmployeur> iter = byDateAndStatutAbsenceEmployeur.iterator();

        Collection<RTTEmployeurDTO> collection = new ArrayList<>();

        while(iter.hasNext()){
            RTTEmployeur employeur = iter.next();
            collection.add(rttEmployeurMap.toRTTEmployeurDTO(employeur));
        }

        return collection;
    }

    public RTTEmployeurDTO getRTTEmployeurByID(Long id){

        RTTEmployeur rttEmployeur = this.rttEmployeurRepo.findById(id).orElseThrow(EntityExistsException::new);

        return rttEmployeurMap.toRTTEmployeurDTO(rttEmployeur);
    }

    public RTTEmployeur createRTT(RTTEmployeurDTO rttEmployeurDTO) {
        applyBusinessLogic(rttEmployeurDTO);

        RTTEmployeur rttEmployeur = rttEmployeurMap.toRTTEmployeur(rttEmployeurDTO);

        return this.rttEmployeurRepo.save(rttEmployeur);

    }

    public void updateRTT(RTTEmployeurDTO rttEmployeurDTO, Long id){

        applyBusinessLogic(rttEmployeurDTO);

        RTTEmployeur rttEmployeur = rttEmployeurRepo.findById(id).orElseThrow(EntityExistsException::new);
        RTTEmployeur rttEmployeurUpdated = rttEmployeurMap.toRTTEmployeur(rttEmployeurDTO);

        if(rttEmployeur.equals(rttEmployeurUpdated)){
            throw new BrokenRuleException("Aucune mise à jour à faire !");
        }

        rttEmployeur = rttEmployeurUpdated;
        this.rttEmployeurRepo.save(rttEmployeur);

    }

    public void deleteRTT(Long id){
        RTTEmployeur rttEmployeur = this.rttEmployeurRepo.findById(id).orElseThrow(EntityExistsException::new);
        rttEmployeurRepo.delete(rttEmployeur);
    }

    private void applyBusinessLogic(RTTEmployeurDTO rttEmployeurDTO) {

        // TODO LOGIC METIER: Le statut de l'absence doit etre en INITIALE
        if(!rttEmployeurDTO.getStatutAbsenceEmployeur().equals(StatutAbsenceEmployeur.INITIALE)){
            throw new BrokenRuleException("Le statut de l'absence doit etre initiale");
        }

        // TODO LOGIC METIER: un RTT employeur doit etre dans le future
        if (!LocalDate.now().isBefore(rttEmployeurDTO.getDate())) {
            throw new BrokenRuleException(("La date du jour de RTT doit être dans le futur"));
        }

        // TODO LOGIC METIER; un RTT employeur ne peut pas etre déclaré sur un jour férié
        if (DateUtils.isRTTOnJourFerie(rttEmployeurDTO.getDate(), JoursOuvresFrance.joursFeries(rttEmployeurDTO.getDate().getYear()))) {
            throw new BrokenRuleException("Le jour RTT posé ne peut pas être un jour férié.");
        }

        // TODO LOGIC METIER: Un RTT employeur ne peut pas etre déclaré sur un jour RTT existant
        if (rttEmployeurRepo.existsByDate(rttEmployeurDTO.getDate())) {
            throw new BrokenRuleException("Le jour RTT posé ne peut pas être un jour RTT existant");
        }

    }

    /**
     *********************** JUST IN CASE ****************************
     * Cette méthode permet de verifier que le nom de l'employeur
     * existe bien en base de données
     * @param employeurName
     */
    private boolean verifyIfEmployeurNameExist(String employeurName) {
        // TODO LOGIC METIER: verifier que le nom de l'employeur existe bien en base
        if (employeeRepo.findByNom(employeurName) == null) {
            return true;
        }
        return false;
    }
}
