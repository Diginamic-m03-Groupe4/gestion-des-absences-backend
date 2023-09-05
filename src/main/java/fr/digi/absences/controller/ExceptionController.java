package fr.digi.absences.controller;

import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.exception.DuplicateIdentifierException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<?> sendEntityNotFoundException(EntityNotFoundException ex){
        return ResponseEntity.badRequest().body(new ErrorMessage("Une entité de la requête ne correpond pas à une colonne de la base"));
    }

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<?> sendBrokenRuleException(BrokenRuleException ex){
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<?> sendDuplicateIdentifierException(DuplicateIdentifierException ex){
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }
}
