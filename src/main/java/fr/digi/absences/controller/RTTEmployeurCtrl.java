package fr.digi.absences.controller;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.dto.RTTEmployeurDTO;
import fr.digi.absences.entity.RTTEmployeur;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.service.JwtService;
import fr.digi.absences.service.RTTService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class RTTEmployeurCtrl {

    private RTTService rttService;
    private final EmployeeRepo employeeRepo;
    private final JwtService jwtService;

    /**
     * @return
     */
//    @Secured("admin")
    @GetMapping // PAGINATION + TARD
    public ResponseEntity<Collection<RTTEmployeur>> getRTTEmployeurList(@RequestParam int annee) {
        Collection<RTTEmployeur> rttEmployeur = rttService.getRTTEmployeur(annee);
        return ResponseEntity.status(200).body(rttEmployeur);
    }

    /**
     * @param id
     * @return
     */
//    @Secured("admin")
    @GetMapping("/{id}")
    public ResponseEntity<RTTEmployeurDTO> getRTTEmployeur(@PathVariable Long id) {
        RTTEmployeurDTO rttEmployeurByID = rttService.getRTTEmployeurByID(id);
        return ResponseEntity.status(200).body(rttEmployeurByID);
    }

    /**
     * @param dtos
     * @return
     */
//    @Secured("admin")
    @PostMapping
    public ResponseEntity<List<RTTEmployeurDTO>> createRTTEmployeur(@CookieValue("AUTH-TOKEN") String token, @RequestBody List<RTTEmployeurDTO> dtos) {
        jwtService.verifyAuthorization(token, Roles.ADMINISTRATEUR);
        return ResponseEntity.ok(rttService.createRTTs(dtos));
    }

    /**
     * @param rttEmployeurDTO
     * @param id
     * @return
     */
//    @Secured("admin")
    @PutMapping()
    public ResponseEntity<RTTEmployeurDTO> updateRTTEmployeur(@CookieValue("AUTH-TOKEN") String token, @RequestBody RTTEmployeurDTO rttEmployeurDTO, @PathVariable Long id) {
        jwtService.verifyAuthorization(token, Roles.ADMINISTRATEUR);
        return ResponseEntity.ok(rttService.updateRTT(rttEmployeurDTO));
    }

    /**
     * @param id
     * @return
     */
//    @Secured("admin")
    @DeleteMapping("/{id}")
    public ResponseEntity<ValidResponse> deleteRTTEmployeur(@CookieValue("AUTH-TOKEN") String token, @PathVariable Long id) {
        jwtService.verifyAuthorization(token, Roles.ADMINISTRATEUR);
        rttService.deleteRTT(id);
        return new ResponseEntity<>(new ValidResponse("Le jour RTT a bien été supprimé"), HttpStatus.OK);
    }
}
