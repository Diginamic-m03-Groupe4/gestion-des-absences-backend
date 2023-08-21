package fr.digi.absences.controller;


import fr.digi.absences.dto.JoursFerieRTTEmployeurDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/v1/jours-feries-rrt-employeur"))
public class JoursFerieEtRTTEmployeurCtrl {

    @GetMapping
    public ResponseEntity<?> getJoursFeriesEtRttEmployeur(@RequestBody JoursFerieRTTEmployeurDTO joursFerieRTTEmployeurDTO){
        return null;
    }
}
