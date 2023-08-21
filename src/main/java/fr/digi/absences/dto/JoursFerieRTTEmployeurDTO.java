package fr.digi.absences.dto;

import fr.digi.absences.entity.JourFerie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JoursFerieRTTEmployeurDTO {
    private List<RTTEmployeurDTO> rttEmployeurList;
    private List<JourFerie> jourFerieList;
}
