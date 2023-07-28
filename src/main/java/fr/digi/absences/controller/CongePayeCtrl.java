package fr.digi.absences.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;

@Controller
@RequestMapping("api/v1/conges")
public class CongePayeCtrl {

    @DeleteMapping
    public void deleteConge(@RequestParam Long id){

    }
}
