package fr.digi.absences.controller;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.service.JourFeriesService;
import fr.digi.absences.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/jours-feries")
public class JoursFerieCtrl {
    private final JourFeriesService jourFeriesService;
    private final JwtService jwtConfig;

    /**
     * @param annee
     * @return
     */
    @GetMapping
    public ResponseEntity<List<JourFerie>> getJoursFeries(@RequestParam int annee) {
        return ResponseEntity.ok(jourFeriesService.joursFeries(annee));
    }

    /**
     * @param jourFerie
     * @return
     */
    @PutMapping
    public ResponseEntity<JourFerie> changeJourFerieIsTravaille(@CookieValue("AUTH-TOKEN") String token, @RequestBody JourFerie jourFerie){
        jwtConfig.verifyAuthorization(token, Roles.ADMINISTRATEUR);
        return ResponseEntity.ok(jourFeriesService.changeJoursFerie(jourFerie));
    }
}
