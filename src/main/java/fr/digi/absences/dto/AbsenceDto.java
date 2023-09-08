package fr.digi.absences.dto;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Data
@Builder
public class AbsenceDto {
    private Long id;
    @NotNull
    private LocalDate dateDebut; // Instant librairie JS => moment.js

    @NotNull
    private LocalDate dateFin;
    private String motif;

    private StatutAbsence status;

    @NotNull
    private TypeConge typeConge;
}
