package fr.digi.absences.controller;

import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.service.JourFeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/v1/jours-feries"))
public class JoursFerieCtrl {
    private final JourFeriesService jourFeriesService;
    @GetMapping
    public ResponseEntity<List<JourFerie>> getJoursFeries(@RequestParam int annee) {
        return ResponseEntity.ok(jourFeriesService.joursFeries(annee));
    }
}
