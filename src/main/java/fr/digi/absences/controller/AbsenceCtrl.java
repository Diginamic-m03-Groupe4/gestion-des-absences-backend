package fr.digi.absences.controller;

import fr.digi.absences.consts.Roles;
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
    public ResponseEntity<AbsenceDto> createAbsence(@CookieValue("AUTH-TOKEN") String token, @RequestBody AbsenceDto absenceDto){
        String email = jwtService.extractEmail(token);
        AbsenceDto absenceDtoRes = absenceSrvc.createAbsence(absenceDto, email);
        return ResponseEntity.status(201).body(absenceDtoRes);
    }

    /**
     * @param id
     * @param absenceDto
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAbsence(@CookieValue("AUTH-TOKEN") String token, @PathVariable long id, @RequestBody AbsenceDto absenceDto){
        String email = jwtService.extractEmail(token);
        absenceSrvc.updateAbsence(id, absenceDto, email);
        return new ResponseEntity<>("Absence mis à jour avec succés", HttpStatus.OK);
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ValidResponse> deleteAbsence(@PathVariable long id){
        absenceSrvc.deleteAbsence(id);
        return new ResponseEntity<>(new ValidResponse("L'absence a été supprimée avec Succès") , HttpStatus.OK);
    }

//    @Secured("MANAGER")
    //tester profil
    @GetMapping("/demandes")
    public ResponseEntity<List<AbsenceDto>> displayListAbsence(@CookieValue("AUTH-TOKEN") String token){
        String email = jwtService.extractEmail(token);
        List<AbsenceDto> absences = this.absenceSrvc.getListAbsence(email);
        return ResponseEntity.status(200).body(absences);
    }

    @PostMapping("/demandes/refused")
    public ResponseEntity<AbsenceDto> refuseAbsence(@CookieValue("AUTH-TOKEN") String token, @RequestParam Long absenceId) {
        jwtService.verifyAuthorization(token, Roles.MANAGER);
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
    public ResponseEntity<AbsenceDto> validateAbsence(@CookieValue("AUTH-TOKEN") String token, @RequestParam Long absenceId) {
        jwtService.verifyAuthorization(token, Roles.MANAGER);
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
