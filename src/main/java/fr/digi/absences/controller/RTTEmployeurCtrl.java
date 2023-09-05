package fr.digi.absences.controller;

import fr.digi.absences.dto.RTTEmployeurDTO;
import fr.digi.absences.entity.RTTEmployeur;
import fr.digi.absences.repository.EmployeeRepo;
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
    public ResponseEntity<List<RTTEmployeurDTO>> createRTTEmployeur(@RequestBody List<RTTEmployeurDTO> dtos) {
        return ResponseEntity.ok(rttService.createRTTs(dtos));
    }

    /**
     * @param rttEmployeurDTO
     * @param id
     * @return
     */
//    @Secured("admin")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRTTEmployeur(@RequestBody RTTEmployeurDTO rttEmployeurDTO, @PathVariable Long id) {
        rttService.updateRTT(rttEmployeurDTO, id);
        return new ResponseEntity<>("Vous avez mis à jour vos jours RTT", HttpStatus.OK);
    }

    /**
     * @param id
     * @return
     */
//    @Secured("admin")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRTTEmployeur(@PathVariable Long id) {
        rttService.deleteRTT(id);
        return new ResponseEntity<>("Le jour RTT a bien été supprimé", HttpStatus.OK);
    }
}
