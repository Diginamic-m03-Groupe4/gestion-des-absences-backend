package fr.digi.absences.controller;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.mapper.AbsenceMap;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.service.AbsenceSrvc;
import fr.digi.absences.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/absence")
public class AbsenceCtrl {

    private AbsenceSrvc absenceSrvc;
    private AbsenceRepo absenceRepo;
    private AbsenceMap absenceMap;
    private JwtService jwtService;

    /**
     * @param annee
     * @return
     */
    @GetMapping
    public ResponseEntity<List<AbsenceDto>> displayAbsences(@CookieValue("AUTH-TOKEN") String token, @RequestParam int annee){
        String email = jwtService.extractEmail(token);
        return ResponseEntity.status(200).body(absenceSrvc.getAbsences(annee, email));
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<AbsenceDto> displayAbsence(@PathVariable long id){
        return ResponseEntity.status(200).body(absenceSrvc.getAbsence(id));
    }

    /**
     * @param absenceDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Absence> createAbsence(@RequestBody AbsenceDto absenceDto){
        Absence absence = absenceSrvc.createAbsence(absenceDto);
        return ResponseEntity.status(201).body(absence);
    }

    /**
     * @param id
     * @param absenceDto
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAbsence(@PathVariable long id, @RequestBody AbsenceDto absenceDto){
        absenceSrvc.updateAbsence(id, absenceDto);
        return new ResponseEntity<>("Absence mis à jour avec succés", HttpStatus.OK);
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAbsence(@PathVariable long id){
        absenceSrvc.deleteAbsence(id);
        return new ResponseEntity<>("L'absence a été supprimée avec Succès", HttpStatus.OK);
    }

//    @Secured("MANAGER")
    //tester profil
    @GetMapping("/demandes/{absenceId}")
    public ResponseEntity<List<AbsenceDto>> displayListAbsence(@PathVariable long absenceId){
        List<AbsenceDto> absences = this.absenceSrvc.getListAbsence(absenceId);
        return ResponseEntity.status(200).body(absences);
    }

    @PostMapping("/demandes/refused")
    public ResponseEntity<AbsenceDto> refuseAbsence(@RequestParam Long absenceId) {
        Optional<Absence> absence = absenceRepo.findById(absenceId);
        if(absence.isPresent() && absence.get().getStatus().equals(StatutAbsence.ATTENTE_VALIDATION)){
            absence.get().setStatus(StatutAbsence.REJETEE);
            absenceRepo.save(absence.get());
            return ResponseEntity.ok(absenceMap.toAbsenceDto(absence.get()));
        } else {
            throw new EntityNotFoundException();
        }
    };

    @PostMapping("/demandes/validated")
    public ResponseEntity<AbsenceDto> validateAbsence(@RequestParam Long absenceId) {
        Optional<Absence> absence = absenceRepo.findById(absenceId);
        if(absence.isPresent() && absence.get().getStatus().equals(StatutAbsence.ATTENTE_VALIDATION)){
            absence.get().setStatus(StatutAbsence.VALIDEE);
            absenceRepo.save(absence.get());
            return ResponseEntity.ok(absenceMap.toAbsenceDto(absence.get()));
        } else {
            throw new EntityNotFoundException();
        }
    };

}
