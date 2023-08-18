package fr.digi.absences.dto;

import fr.digi.absences.consts.StatutAbsenceEmployeur;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.Employee;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RTTEmployeurDTO {

    @NotNull
    private LocalDate date;

    private String libelle;

    // BEAN VALIDATION DEFAULT VALUE = INITIALE
    private StatutAbsenceEmployeur statutAbsenceEmployeur;

}
