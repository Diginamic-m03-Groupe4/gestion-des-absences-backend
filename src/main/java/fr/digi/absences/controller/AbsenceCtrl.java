package fr.digi.absences.controller;

import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.service.AbsenceSrvc;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("absence")
@AllArgsConstructor
public class AbsenceCtrl {

    private AbsenceSrvc absenceSrvc;

    @PostMapping
    public ResponseEntity<Absence> createAbsence(@RequestBody AbsenceDto absenceDto){
        // APPLICATION DES LOGIQUES METIERS
        Absence absence = absenceSrvc.createAbsence(absenceDto);
        return ResponseEntity.status(201).body(absence);
    }
}
