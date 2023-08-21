package fr.digi.absences.controller;

import fr.digi.absences.dto.RTTEmployeurDTO;
import fr.digi.absences.entity.RTTEmployeur;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.service.RTTService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController("api/v1/admin")
@AllArgsConstructor
public class RTTEmployeurCtrl {

    private RTTService rttService;
    private final EmployeeRepo employeeRepo;

    @Secured("admin")
    @GetMapping("/list_rtt_employeur") // PAGINATION + TARD
    public ResponseEntity<Collection<RTTEmployeur>> getRTTEmployeurList(){
        Collection<RTTEmployeur> rttEmployeur = rttService.getRTTEmployeur();
        return ResponseEntity.status(200).body(rttEmployeur);
    }

    @Secured("admin")
    @GetMapping("/{id}")
    public ResponseEntity<RTTEmployeurDTO> getRTTEmployeur(@PathVariable Long id){
        RTTEmployeurDTO rttEmployeurByID = rttService.getRTTEmployeurByID(id);
        return ResponseEntity.status(200).body(rttEmployeurByID);
    }

    @Secured("admin")
    @PostMapping
    public ResponseEntity<RTTEmployeur> createRTTEmployeur(@RequestBody RTTEmployeurDTO rttEmployeurDTO){
        RTTEmployeur rtt = this.rttService.createRTT(rttEmployeurDTO);
        return ResponseEntity.status(201).body(rtt);
    }

    @Secured("admin")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRTTEmployeur(@RequestBody RTTEmployeurDTO rttEmployeurDTO, @PathVariable Long id){
        this.rttService.updateRTT(rttEmployeurDTO, id);
        return new ResponseEntity<>("Vous avez mis à jour vos jours RTT", HttpStatus.OK);
    }

    @Secured("admin")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRTTEmployeur(@PathVariable Long id){
        this.rttService.deleteRTT(id);
        return new ResponseEntity<>("Le jour RTT a bien été supprimé", HttpStatus.OK);
    }
}
