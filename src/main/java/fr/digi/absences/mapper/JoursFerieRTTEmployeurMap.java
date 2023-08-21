package fr.digi.absences.mapper;

import fr.digi.absences.dto.JoursFerieRTTEmployeurDTO;
import fr.digi.absences.dto.RTTEmployeurDTO;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.entity.RTTEmployeur;
import fr.digi.absences.service.JourFeriesService;
import fr.digi.absences.service.RTTService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JoursFerieRTTEmployeurMap {

    private RTTEmployeur rttEmployeur;

    private RTTService rttService;
    private JourFeriesService jourFeriesService;
    private RTTEmployeurDTO rttEmployeurDTO;
    private JoursFerieRTTEmployeurDTO joursFerieRTTEmployeurDTO;

    public List<RTTEmployeur> toRttEmployeur() {
        return Collections.emptyList();
    }

    public List<JourFerie> toJoursFeries() {
        return joursFerieRTTEmployeurDTO.getJourFerieList();
    }

    public List<RTTEmployeur> modifyRttEmployeur() {
        return Collections.emptyList();
    }

    public List<JourFerie> modifyJoursFerie() {
        return Collections.emptyList();
    }

    public JoursFerieRTTEmployeurDTO toJoursFerieRTTEmployeurDTO() {
        return null;
    }
}
