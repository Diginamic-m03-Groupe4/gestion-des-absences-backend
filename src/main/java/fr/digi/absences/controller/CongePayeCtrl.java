package fr.digi.absences.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("conges")
public class CongePayeCtrl {

    @DeleteMapping
    public void deleteConge(@RequestParam Long id){

    }
}
