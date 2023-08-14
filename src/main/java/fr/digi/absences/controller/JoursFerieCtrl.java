package fr.digi.absences.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class JoursFerieCtrl {

    @GetMapping("api/v1/jours-feries")
    public List<LocalDate> getJoursFeries() {
        return null;
    }
}
