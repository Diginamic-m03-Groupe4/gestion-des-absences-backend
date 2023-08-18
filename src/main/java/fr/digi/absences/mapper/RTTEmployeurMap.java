package fr.digi.absences.mapper;

import fr.digi.absences.dto.RTTEmployeurDTO;
import fr.digi.absences.entity.RTTEmployeur;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.repository.RTTEmployeurRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RTTEmployeurMap {
    private RTTEmployeur rttEmployeur;

    public RTTEmployeur toRTTEmployeur(RTTEmployeurDTO rttEmployeurDTO){
        return RTTEmployeur.builder()
                .date(rttEmployeurDTO.getDate())
                .libelle(rttEmployeurDTO.getLibelle())
                .statutAbsenceEmployeur(rttEmployeurDTO.getStatutAbsenceEmployeur())
                .build();
    }

    public RTTEmployeurDTO toRTTEmployeurDTO(RTTEmployeur rttEmployeur){
        return RTTEmployeurDTO.builder()
                .date(rttEmployeur.getDate())
                .libelle(rttEmployeur.getLibelle())
                .statutAbsenceEmployeur(rttEmployeur.getStatutAbsenceEmployeur())
                .build();
    }

}
