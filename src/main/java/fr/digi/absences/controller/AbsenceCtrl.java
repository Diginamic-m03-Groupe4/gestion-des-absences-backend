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
@RequestMapping("/api/v1/absence")
@AllArgsConstructor
public class AbsenceCtrl {

    private AbsenceSrvc absenceSrvc;

    @GetMapping("/{id}")
    public ResponseEntity<Absence> displayAbsence(@PathVariable long id){
        Absence absence = this.absenceSrvc.getAbsence(id);
        return ResponseEntity.status(200).body(absence);
    }

    @PostMapping
    public ResponseEntity<Absence> createAbsence(@RequestBody AbsenceDto absenceDto){
        // APPLICATION DES LOGIQUES METIERS
        Absence absence = this.absenceSrvc.createAbsence(absenceDto);
        return ResponseEntity.status(201).body(absence);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAbsence(@PathVariable long id, @RequestBody AbsenceDto absenceDto){
        this.absenceSrvc.updateAbsence(id, absenceDto);
        return new ResponseEntity<>("Absence mis à jour avec succés", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAbsence(@PathVariable long id, @RequestBody AbsenceDto absenceDto){
        this.absenceSrvc.deleteAbsence(id);
        return new ResponseEntity<>("L'absence a été supprimée avec Succès", HttpStatus.OK);
    }

    //id manager by url
    @GetMapping("/e/{id}")
    public ResponseEntity<List<Absence>> displayListAbsence(@PathVariable long id){
        List<Absence> absences = this.absenceSrvc.getListAbsence(3);
        return ResponseEntity.status(200).body(absences);
    }

//    @PostMapping("/e")
//    public ResponseEntity
}
