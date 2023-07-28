package fr.digi.absences.controller;

import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("absence")
@AllArgsConstructor
public class AbsenceCtrl {

    @PostMapping
    public ResponseEntity<?> createAbsence(@RequestBody AbsenceDto absenceDto){
        return null;
    }
}
