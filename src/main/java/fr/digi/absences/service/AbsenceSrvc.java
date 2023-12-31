package fr.digi.absences.service;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.mapper.AbsenceMap;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AbsenceSrvc {

    private EmployeeRepo employeeRepo;
    private AbsenceRepo absenceRepo;
    private JourFeriesService jourFeriesService;
    private AbsenceMap absenceMap;

    /**
     * @param id
     * @return
     */
    public AbsenceDto getAbsence(long id) {
        return absenceMap.toAbsenceDto(absenceRepo.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    /**
     * @param absenceDto
     * @return
     */
    public AbsenceDto createAbsence(AbsenceDto absenceDto, String email) {
        applyCreationLogic(absenceDto, email);
        // CREATION DE l'ABSENCE AVEC LE MAPPER ABSENCE
        Absence absence = absenceMap.toAbsence(absenceDto, email);
        Absence save = absenceRepo.save(absence);
        return absenceMap.toAbsenceDto(save);
    }

    /**
     * @param id
     * @param absenceDto
     */
    public void updateAbsence(long id, AbsenceDto absenceDto, String email) {
        Absence absence = absenceRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        applyModificationLogic(absenceDto, absence, email);
        absenceMap.modifyAbsence(absence, absenceDto);
        this.absenceRepo.save(absence);
    }

    /**
     * @param id
     */
    public void deleteAbsence(long id) {
        Absence absence = absenceRepo.findById(id).orElse(null);
        if (absence == null) {
            throw new EntityNotFoundException();
        }
        absenceRepo.delete(absence);
    }

    public List<AbsenceDto> getListAbsence(String email) {
        long id = employeeRepo.findByEmail(email).orElseThrow(EntityNotFoundException::new).getDepartement().getId();
        return absenceRepo.getListAbsencesDemandesOfDepartement(id).stream()
                .map(absenceMap::toAbsenceDto)
                .toList();
    }

    /**
     * @param absenceDto
     */
    private void applyModificationLogic(AbsenceDto absenceDto, Absence absence, String email) {
        applyCommonLogic(absenceDto, email);
        if (!(absenceDto.getStatus() == StatutAbsence.INITIALE || absenceDto.getStatus() == StatutAbsence.REJETEE)) {
            throw new BrokenRuleException("Vous ne pouvez modifier une absence qu'au status initiale ou rejetée");
        }
        if (absence.getDateDebut().isBefore(LocalDate.now())){
            throw new BrokenRuleException("Vous ne pouvez pas modifier un absence dans le passé");
        }
        int nbAbsences = absenceRepo.getNbAbsencesBetweenDateDebutAndDateFinWOAbs(absenceDto.getDateDebut(), absenceDto.getDateFin().plusDays(1), email, absenceDto.getId());
        if (nbAbsences > 0) {
            throw new BrokenRuleException("Il y a " + nbAbsences + " absences qui sont dans le créneau de l'absence que vous souhaitez créer");
        }
        absenceDto.setStatus(StatutAbsence.INITIALE);
    }

    /**
     * @param absenceDto
     */
    private void applyCommonLogic(AbsenceDto absenceDto, String email) {
        if (!LocalDate.now().isBefore(absenceDto.getDateDebut())) {
            throw new BrokenRuleException("La Date du début de l'absence n'est pas supérieure à la date du jour");
        }
        if (!DateUtils.isBusinessDay(absenceDto.getDateDebut()) || !DateUtils.isBusinessDay(absenceDto.getDateFin())) {
            throw new BrokenRuleException("L'absence ne peut commencer ou finir sur un week-end");
        }
        if (absenceDto.getDateFin().isBefore(absenceDto.getDateDebut())) {
            throw new BrokenRuleException("La date de fin doit être superieure ou égale à la date de début");
        }
        if (absenceDto.getTypeConge().equals(TypeConge.SANS_SOLDE) && absenceDto.getMotif().isBlank()) {
            throw new BrokenRuleException("Il faut renseigner le motif du congé");
        }
        if (DateUtils.isOnJourFerie(absenceDto.getDateDebut(), absenceDto.getDateFin(), jourFeriesService.joursFeries(absenceDto.getDateDebut().getYear()))) {
            throw new BrokenRuleException("L'absence ne peut chevaucher un jour férié. Si vous souhaitez créer une absence chevauchant un jour férié, créez une absence avant et après le jour férié");
        }
    }

    private void applyCreationLogic(AbsenceDto absenceDto, String email){
        applyCommonLogic(absenceDto, email);
        int nbAbsences = absenceRepo.getNbAbsencesBetweenDateDebutAndDateFin(absenceDto.getDateDebut(), absenceDto.getDateFin().plusDays(1), email);
        if (nbAbsences > 0) {
            throw new BrokenRuleException("Il y a " + nbAbsences + " absences qui sont dans le créneau de l'absence que vous souhaitez créer");
        }

    }

    /**
     * @param annee
     * @return
     */
    public List<AbsenceDto> getAbsences(int annee, String email) {
        log.info(email);
        return absenceRepo.findAllByAnnee(annee, email).stream()
                .map(absenceMap::toAbsenceDto)
                .toList();
    }
}
