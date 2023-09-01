package fr.digi.absences.controller;

import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.service.AbsenceSrvc;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/absence")
public class AbsenceCtrl {

    private AbsenceSrvc absenceSrvc;

    /**
     * @param annee
     * @return
     */
    @GetMapping
    public ResponseEntity<List<AbsenceDto>> displayAbsences(@RequestParam int annee){
        return ResponseEntity.status(200).body(absenceSrvc.getAbsences(annee));
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
}
