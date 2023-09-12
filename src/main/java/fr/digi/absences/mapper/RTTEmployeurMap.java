package fr.digi.absences.mapper;

import fr.digi.absences.consts.StatutAbsenceEmployeur;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.dto.RTTEmployeurDTO;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.RTTEmployeur;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class RTTEmployeurMap {
    private RTTEmployeur rttEmployeur;

    public RTTEmployeur toRTTEmployeur(RTTEmployeurDTO rttEmployeurDTO){
        return RTTEmployeur.builder()
                .date(rttEmployeurDTO.getDate())
                .libelle(rttEmployeurDTO.getLibelle())
                .statutAbsenceEmployeur(StatutAbsenceEmployeur.INITIALE)
                .build();
    }

    public RTTEmployeurDTO toRTTEmployeurDTO(RTTEmployeur rttEmployeur){
        return RTTEmployeurDTO.builder()
                .id(rttEmployeur.getId())
                .date(rttEmployeur.getDate())
                .libelle(rttEmployeur.getLibelle())
                .statutAbsenceEmployeur(rttEmployeur.getStatutAbsenceEmployeur())
                .build();
    }

    public void modifyRTTEmployeur(RTTEmployeur from, RTTEmployeurDTO to){
        from.setStatutAbsenceEmployeur(to.getStatutAbsenceEmployeur());
        from.setStatutAbsenceEmployeur(StatutAbsenceEmployeur.INITIALE);
        from.setDate(to.getDate());
        from.setLibelle(to.getLibelle());
    }

}
