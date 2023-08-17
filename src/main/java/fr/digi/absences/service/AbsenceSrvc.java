package fr.digi.absences.service;

import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.mapper.AbsenceMap;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AbsenceSrvc {

    private EmployeeRepo employeeRepo;
    private AbsenceRepo absenceRepo;
    private JourFeriesService jourFeriesService;

    public Absence getAbsence(long id) {
        Optional<Absence> absenceFound = this.absenceRepo.findById(id);
        if (absenceFound.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return absenceFound.get();
    }

    public Absence createAbsence(AbsenceDto absenceDto) {
        applyBusinessLogic(absenceDto);

        // CREATION DE l'ABSENCE AVEC LE MAPPER ABSENCE
        AbsenceMap absenceMap = new AbsenceMap();
        Absence absence = absenceMap.toAbsence(absenceDto);

        return this.absenceRepo.save(absence);

    }

    public void updateAbsence(long id, AbsenceDto absenceDto) {

        applyBusinessLogic(absenceDto);

        Absence absence = this.absenceRepo.findById(id).orElse(null);
        if (absence == null) {
            throw new EntityNotFoundException();
        }

        AbsenceMap absenceMap = new AbsenceMap();
        Absence absenceUpdated = absenceMap.toAbsence(absenceDto);

        if(!absence.equals(absenceUpdated)){
            absence = absenceUpdated;
        }

        this.absenceRepo.save(absence);
    }

    public void deleteAbsence(long id) {
        Absence absence = this.absenceRepo.findById(id).orElse(null);
        if (absence == null) {
            throw new EntityNotFoundException();
        }
        this.absenceRepo.delete(absence);
    }


    private void applyBusinessLogic(AbsenceDto absenceDto) {
        // TODO LOGIC METIER
        if (!LocalDate.now().isBefore(absenceDto.getDateDebut())) {
            throw new BrokenRuleException("La Date du début de l'absence n'est pas supérieure à la date du jour");
        }

        // TODO LOGIC METIER
        if (absenceDto.getDateFin().isBefore(absenceDto.getDateDebut())) {
            throw new BrokenRuleException("La date de fin doit être superieure ou égale à la date de début");
        }

        // TODO LOGIC METIER
        if (absenceDto.getTypeConge().equals(TypeConge.SANS_SOLDE) && absenceDto.getMotif().isBlank()) {
            throw new BrokenRuleException("Il faut renseigner le motif du congé");
        }

        // TODO LOGIC METIER date de début doit etre un jour ouvré et ne doit pas etre un jour ferié
        // TODO LOGIC METIER date de fin doit etre un jour ouvré et ne doit pas etre un jour ferié
        if (DateUtils.isOnJourFerie(absenceDto.getDateDebut(), absenceDto.getDateFin(), jourFeriesService.joursFeries(absenceDto.getDateDebut().getYear()).stream().map(JourFerie::getDate).toList())){
            throw new BrokenRuleException("L'absence ne peut chevaucher un jour férié. Si vous souhaitez créer une absence chevauchant un jour férié, créez une absence avant et après le jour férié");
        };
        int nbAbsences = absenceRepo.getNbAbsencesBetweenDateDebutAndDateFin(absenceDto.getDateDebut(), absenceDto.getDateFin(), absenceDto.getEmail());
        if(nbAbsences > 0){
            throw new BrokenRuleException("Il y a " + nbAbsences + " absences qui sont dans le créneau de l'absence que vous souhaitez créer");
        }
    }
}
